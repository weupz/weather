package weather.feature.weather

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.threeten.bp.Duration
import org.threeten.bp.ZonedDateTime
import weather.mvi.DefaultPresenter
import weather.scheduler.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class WeatherPresenter @Inject constructor(
    private val repository: Repository,
    private val schedulers: Schedulers
) : DefaultPresenter<State, Event>(), WeatherViewModel {

    private val subject = PublishSubject.create<Unit>()

    override fun retry() {
        subject.onNext(Unit)
    }

    override val states: Flowable<State> =
        super.states.distinctUntilChanged().observeOn(schedulers.main)

    override fun createIntents(): Flowable<Event> {
        val stop = lifecycleEvents.filter { it === LifecycleEvent.Detach }
            .toFlowable(BackpressureStrategy.LATEST)
        return state {
            val attach = lifecycleEvents.filter { it === LifecycleEvent.Attach }
            Observable.merge(attach, subject)
        }
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
}
