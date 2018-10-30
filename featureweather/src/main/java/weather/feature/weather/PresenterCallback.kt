package weather.feature.weather

import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import org.threeten.bp.Duration
import org.threeten.bp.ZonedDateTime
import weather.mvi.DefaultPresenter
import weather.mvi.DefaultPresenter.IntentFactory
import weather.scheduler.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class PresenterCallback @Inject constructor(
    private val repository: Repository,
    private val schedulers: Schedulers
) : DefaultPresenter.Callback<WeatherView, State, Event> {

    override fun createIntents(intentFactory: IntentFactory<WeatherView, State>): Flowable<Event> {
        val stop = intentFactory.intent { it.stopSync() }
        return intentFactory.intentState { it.startSync() }
            .flatMap { (_, state) ->
                val now = ZonedDateTime.now()
                if (state.data == null ||
                    state.errorCode > 0 ||
                    now.isAfter(state.data.nextLoad) ||
                    now.isEqual(state.data.nextLoad)) {
                    Flowable.just(Unit)
                } else {
                    Flowable.timer(
                        Duration.between(now, state.data.nextLoad).seconds,
                        TimeUnit.SECONDS,
                        schedulers.computation
                    )
                }
            }
            .switchMap { repository.load().takeUntil(stop) }
    }

    override fun compose(events: Flowable<Event>): Flowable<State> {
        return events.scan(State()) { state, event ->
            when (event) {
                Event.Loading -> {
                    state.copy(loading = true, errorCode = 0, errorMessage = null)
                }
                is Event.Success -> {
                    state.copy(loading = false, data = event.data)
                }
                is Event.Error -> {
                    state.copy(
                        errorCode = event.code,
                        errorMessage = event.message,
                        loading = false
                    )
                }
            }
        }
    }

    override fun subscribeToStates(states: Flowable<State>, view: WeatherView): Disposable {
        return states.distinctUntilChanged().observeOn(schedulers.main)
            .subscribe { view.renderState(it) }
    }
}
