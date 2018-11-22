package weather.mvi

import io.reactivex.Flowable
import io.reactivex.disposables.Disposable

interface ViewModel<S> {
    val states: Flowable<S>
}

inline fun <reified S, reified T : S> ViewModel<T>.subscribe(noinline consumer: (S) -> Unit): Disposable {
    return states.subscribe(consumer)
}
