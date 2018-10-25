package weather.mvi

import io.reactivex.subjects.Subject
import io.reactivex.subscribers.DisposableSubscriber

internal class DisposableStateSubscriber<E>(
    private val subject: Subject<E>
) : DisposableSubscriber<E>() {

    override fun onComplete() {
        // State never completes
    }

    override fun onNext(t: E) {
        subject.onNext(t)
    }

    override fun onError(e: Throwable) {
        throw RuntimeException("State shouldn't reach error", e)
    }
}
