package weather.mvi

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.io.Closeable

abstract class DefaultPresenter<S, A> : MviPresenter, Closeable, ViewModel<S> {

    private var disposeOnDestroy: Disposable? = null
    private val stateSubject = BehaviorSubject.create<S>()
    private val lifeSubject = PublishSubject.create<LifecycleEvent>()

    override val states: Flowable<S> = stateSubject.toFlowable(BackpressureStrategy.LATEST)
    protected val lifecycleEvents: Observable<LifecycleEvent> = lifeSubject

    override fun attachView() {
        if (this.disposeOnDestroy == null) {
            initDisposeOnDestroy()
        }
        lifeSubject.onNext(LifecycleEvent.Attach)
    }

    private fun initDisposeOnDestroy() {
        this.disposeOnDestroy = compose(createIntents())
            .subscribeWith(DisposableStateSubscriber(stateSubject))
    }

    override fun detachView() {
        lifeSubject.onNext(LifecycleEvent.Detach)
    }

    override fun close() {
        destroy()
    }

    private fun destroy() {
        disposeOnDestroy?.dispose()
        disposeOnDestroy = null
    }

    protected open fun createIntents(): Flowable<A> = Flowable.empty()

    protected open fun compose(events: Flowable<A>): Flowable<S> = Flowable.empty()

    protected fun <I> state(intent: () -> Observable<I>): Flowable<IntentState<I, S>> {
        return intent()
            .toFlowable(BackpressureStrategy.LATEST)
            .flatMap {
                Flowable.zip(
                    Flowable.just(it),
                    states.take(1),
                    BiFunction { event: I, state: S -> IntentState(event, state) }
                )
            }
    }

    data class IntentState<I, S> internal constructor(val intent: I, val state: S)

    enum class LifecycleEvent { Attach, Detach }
}
