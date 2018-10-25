package weather.feature.weather

import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import org.threeten.bp.LocalDate
import org.threeten.bp.ZonedDateTime
import weather.mvi.DefaultPresenter
import weather.mvi.DefaultPresenter.IntentFactory
import weather.scheduler.Schedulers
import java.util.Random
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.min

internal class PresenterCallback @Inject constructor(
    private val repository: Repository,
    private val schedulers: Schedulers
) : DefaultPresenter.Callback<WeatherView, State, Event> {

    private fun eventWithNextLoad(data: Event.Data): Flowable<Event> {
        val event = Flowable.just(data)
        val now = ZonedDateTime.now()
        val delay = when {
            now.isBefore(data.sunrise) -> {
                data.sunrise.toEpochSecond() - now.toEpochSecond()
            }
            now.isBefore(data.sunset) -> {
                data.sunrise.toEpochSecond() - now.toEpochSecond()
            }
            else -> {
                val startOfNextDay = now
                    .toLocalDate()
                    .plusDays(1)
                    .atStartOfDay(now.zone)
                    .toEpochSecond()
                startOfNextDay - now.toEpochSecond()
            }
        }
        val randomDelay = min(delay, TWO_HOURS) + Random().nextInt(MAX_RANDOM)
        val nextLoad = Flowable.timer(randomDelay, TimeUnit.SECONDS, schedulers.computation)
            .flatMap { _ -> repository.load() }
        return Flowable.merge(event, nextLoad)
    }

    override fun createIntents(intentFactory: IntentFactory<WeatherView, State>): Flowable<Event> {
        val load = intentFactory.intentState { it.load() }
            .filter { (_, state) ->
                state.time.let {
                    it == null ||
                        it.toLocalDate() != LocalDate.now() ||
                        it.plusDays(1).isBefore(ZonedDateTime.now())
                }
            }
            .switchMap { repository.load() }
        val data = repository.data().switchMap {
            if (it is Event.Data) {
                eventWithNextLoad(it)
            } else {
                Flowable.just(it)
            }
        }
        return Flowable.merge(load, data)
    }

    override fun compose(events: Flowable<Event>): Flowable<State> {
        return events.scan(State()) { state, event ->
            when (event) {
                Event.Loading -> {
                    state.copy(loading = true, errorCode = 0, errorMessage = null)
                }
                Event.Refreshing -> {
                    state.copy(refreshing = true, errorCode = 0, errorMessage = null)
                }
                Event.Success -> {
                    state.copy(loading = false, refreshing = false)
                }
                is Event.Data -> {
                    state.copy(
                        city = event.city,
                        country = event.country,
                        weather = event.weather,
                        weatherIconUri = event.weatherIconUri,
                        temperature = event.temperature,
                        time = event.time
                    )
                }
                is Event.Error -> {
                    state.copy(
                        errorCode = event.code,
                        errorMessage = event.message,
                        loading = false,
                        refreshing = false
                    )
                }
            }
        }
    }

    override fun subscribeToStates(states: Flowable<State>, view: WeatherView): Disposable {
        return states.distinctUntilChanged().observeOn(schedulers.main)
            .subscribe { view.renderState(it) }
    }

    private companion object {
        private const val MAX_RANDOM = 30 * 60
        private const val TWO_HOURS: Long = 2 * 60 * 60
    }
}
