package weather.feature.weather

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function
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

    private val subject = PublishSubject.create<Any>()
    private val retry: Observable<Any> inline get() = subject.filter { it == RETRY }
    private val toggleUnit: Observable<Any> inline get() = subject.filter { it == TOGGLE_UNIT }

    override fun retry() {
        subject.onNext(RETRY)
    }

    override fun toggleUnitType() {
        subject.onNext(TOGGLE_UNIT)
    }

    override val states: Flowable<State> =
        super.states.distinctUntilChanged().observeOn(schedulers.main)

    private val repeater: Function<Event, Flowable<Event>> = Function { event ->
        if (event is Event.Success) {
            val now = ZonedDateTime.now()
            val load = if (now.isBefore(event.data.nextLoad)) {
                val delay = Duration.between(now, event.data.nextLoad)
                Single.timer(delay.seconds, TimeUnit.SECONDS, schedulers.computation)
            } else {
                Single.just(Unit)
            }
                .zipWith(
                    lifecycleEvents.filter { it == LifecycleEvent.Attach }.firstOrError(),
                    BiFunction { a: Any, _: LifecycleEvent -> a }
                )
                .flatMapPublisher { load() }
            Flowable.concat(Flowable.just(event), load)
        } else {
            Flowable.just(event)
        }
    }

    private fun load(): Flowable<Event> {
        return repository.load().flatMap(repeater)
    }

    override fun createIntents(): Flowable<Event> {
        val sync = retry.toFlowable(BackpressureStrategy.LATEST)
            .startWith(Unit)
            .switchMap { load() }
        val toggleUnit = toggleUnit.map<Event> { Event.ToggleUnit }
            .toFlowable(BackpressureStrategy.LATEST)
        return Flowable.merge(sync, toggleUnit)
    }

    override fun compose(events: Flowable<Event>): Flowable<State> {
        return events.scan(State()) { state, event ->
            when (event) {
                Event.ToggleUnit -> {
                    state.copy(
                        unitType = if (state.unitType === State.UnitType.Metric) {
                            State.UnitType.Imperial
                        } else {
                            State.UnitType.Metric
                        }
                    )
                }
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

    private companion object {
        private const val RETRY = 1
        private const val TOGGLE_UNIT = 2
    }
}
