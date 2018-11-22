package weather.mvi.lifecycle

import androidx.lifecycle.GenericLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import weather.mvi.MviPresenter

class PresenterManager private constructor(
    private val presenter: MviPresenter
) : GenericLifecycleObserver {

    override fun onStateChanged(source: LifecycleOwner?, event: Lifecycle.Event?) {
        if (event == Lifecycle.Event.ON_START) {
            presenter.attachView()
        } else if (event == Lifecycle.Event.ON_STOP) {
            presenter.detachView()
        }
    }

    fun assign(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
    }

    companion object {

        fun create(presenter: MviPresenter): PresenterManager {
            return PresenterManager(presenter)
        }
    }
}
