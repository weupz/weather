package weather.mvi

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.io.Closeable

class DefaultPresenter<in V : MviView, S, A> internal constructor(
    private val callback: Callback<V, S, A>
) : MviPresenter<V>, Closeable {

    private var disposeOnDestroy: CompositeDisposable? = null
    private var disposeOnDetach: CompositeDisposable? = null

    private val eventSubject = PublishSubject.create<A>()
    private val stateSubject = BehaviorSubject.create<S>()

    private val intentFactory = IntentFactory<V, S>()

    override fun attachView(view: V) {
        if (this.disposeOnDestroy == null) {
            initDisposeOnDestroy()
        }
        initDisposeOnDetach(view)
    }

    private fun initDisposeOnDetach(view: V) {
        val disposeOnDetach = CompositeDisposable()
        val states = stateSubject.toFlowable(BackpressureStrategy.LATEST)
        disposeOnDetach.add(callback.subscribeToStates(states, view))
        intentFactory.subscribe(disposeOnDetach, view, stateSubject)
        this.disposeOnDetach = disposeOnDetach
    }

    private fun initDisposeOnDestroy() {
        val disposables = CompositeDisposable()
        val composedStates = callback.compose(eventSubject.toFlowable(BackpressureStrategy.BUFFER))
        disposables.add(composedStates.subscribeWith(DisposableStateSubscriber(stateSubject)))
        val intents = callback.createIntents(intentFactory)
        disposables.add(intents.subscribeWith(DisposableStateSubscriber(eventSubject)))
        this.disposeOnDestroy = disposables
    }

    override fun detachView() {
        disposeOnDetach?.dispose()
        disposeOnDetach = null
    }

    override fun close() {
        destroy()
    }

    private fun destroy() {
        disposeOnDestroy?.dispose()
        disposeOnDestroy = null
    }

    class IntentFactory<V, S> {
        private val intentCreators =
            mutableListOf<Pair<PublishSubject<Any>, (V) -> Observable<*>>>()
        private val intentStateCreators =
            mutableListOf<Pair<PublishSubject<IntentState<Any, *>>, (V) -> Observable<*>>>()

        @Suppress("UNCHECKED_CAST")
        fun <I> intent(intentCreator: (V) -> Observable<I>): Flowable<I> {
            val relay = PublishSubject.create<I>()
            intentCreators.add(Pair(relay as PublishSubject<Any>, intentCreator))
            return relay.toFlowable(BackpressureStrategy.LATEST) as Flowable<I>
        }

        @Suppress("UNCHECKED_CAST")
        fun <I> intentState(intentCreator: (V) -> Observable<I>): Flowable<IntentState<I, S>> {
            val relay = PublishSubject.create<IntentState<I, S>>()
            intentStateCreators.add(
                Pair(relay as PublishSubject<IntentState<Any, *>>, intentCreator)
            )
            return relay.toFlowable(BackpressureStrategy.LATEST) as Flowable<IntentState<I, S>>
        }

        internal fun subscribe(
            disposable: CompositeDisposable,
            view: V,
            states: Observable<S>
        ) {
            intentCreators.forEach {
                disposable.add(it.second(view).subscribeWith(DisposableIntentObserver(it.first)))
            }
            if (!intentStateCreators.isEmpty()) {
                val mapper = IntentStateMapper(states)
                intentStateCreators.forEach {
                    val observable = it.second(view).flatMap(mapper)
                    disposable.add(observable.subscribeWith(DisposableIntentObserver(it.first)))
                }
            }
        }

        private class IntentStateMapper<S>(
            private val states: Observable<S>
        ) : Function<Any, Observable<IntentState<Any, S>>> {
            override fun apply(it: Any): Observable<IntentState<Any, S>> {
                return Observable.combineLatest<Any, S, IntentState<Any, S>>(
                    Observable.just(it),
                    states.take(1),
                    BiFunction { intent, state -> IntentState(intent, state) }
                )
            }
        }
    }

    interface Callback<V : MviView, S, E> {
        fun createIntents(intentFactory: IntentFactory<V, S>): Flowable<E> = Flowable.empty()
        fun compose(events: Flowable<E>): Flowable<S>
        fun subscribeToStates(states: Flowable<S>, view: V): Disposable
    }

    data class IntentState<out I, out S>(val intent: I, val state: S)
}
