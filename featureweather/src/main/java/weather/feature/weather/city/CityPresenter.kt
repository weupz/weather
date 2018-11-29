package weather.feature.weather.city

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import weather.di.ActivityScope
import weather.mvi.DefaultPresenter
import weather.scheduler.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@ActivityScope
internal class CityPresenter @Inject constructor(
    private val repository: Repository,
    private val schedulers: Schedulers
) : DefaultPresenter<State, Event>(), CityViewModel {

    private val relay = PublishSubject.create<Any>()

    override fun search(name: String) {
        relay.onNext(name)
    }

    override fun updateCity(id: Long) {
        relay.onNext(id)
    }

    override val states: Flowable<State> =
        super.states.distinctUntilChanged().observeOn(schedulers.main)

    @Suppress("UNCHECKED_CAST")
    override fun createIntents(): Flowable<Event> {
        val search = (relay.filter { it is String } as Observable<String>)
            .debounce(DEBOUNCE, TimeUnit.MILLISECONDS, schedulers.computation)
            .toFlowable(BackpressureStrategy.LATEST)
            .switchMap { repository.search(it) }
        val update = (relay.filter { it is Long } as Observable<Long>)
            .toFlowable(BackpressureStrategy.LATEST)
            .switchMap { repository.updateCity(it) }
        val populate = lifecycleEvents.filter { it == LifecycleEvent.Attach }
            .toFlowable(BackpressureStrategy.LATEST)
            .flatMap { repository.populateCities() }
        return Flowable.merge(search, update, populate)
    }

    override fun compose(events: Flowable<Event>): Flowable<State> {
        return events.scan(State()) { state, event ->
            when (event) {
                Event.UpdatingCity -> state
                Event.CityUpdated -> state
                Event.Searching -> state.copy(inProgress = true)
                is Event.Success -> state.copy(inProgress = false, data = event.data)
            }
        }
    }

    private companion object {
        private const val DEBOUNCE: Long = 300
    }
}
