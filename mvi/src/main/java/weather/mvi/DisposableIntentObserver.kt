package weather.mvi

import io.reactivex.observers.DisposableObserver
import io.reactivex.subjects.Subject

internal class DisposableIntentObserver<E>(
    private val subject: Subject<E>
) : DisposableObserver<E>() {

    override fun onComplete() {
        // Intent never completes
    }

    override fun onNext(t: E) {
        subject.onNext(t)
    }

    override fun onError(e: Throwable) {
        throw RuntimeException("Intent shouldn't reach error", e)
    }
}
