package weather.mvi.lifecycle

import androidx.lifecycle.GenericLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import weather.mvi.MviPresenter
import weather.mvi.MviView

class PresenterManager<V : MviView> private constructor(
    private val presenter: MviPresenter<V>,
    private val view: V?,
    private val viewProvider: () -> V
) : GenericLifecycleObserver {

    override fun onStateChanged(source: LifecycleOwner?, event: Lifecycle.Event?) {
        if (event == Lifecycle.Event.ON_START) {
            presenter.attachView(view ?: viewProvider())
        } else if (event == Lifecycle.Event.ON_STOP) {
            presenter.detachView()
        }
    }

    fun assign(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
    }

    companion object {

        private val errorViewProvider: () -> Nothing = {
            throw RuntimeException("View is not provided!")
        }

        fun <V : MviView> create(presenter: MviPresenter<V>, view: V): PresenterManager<V> {
            return PresenterManager(presenter, view, errorViewProvider)
        }

        fun <V : MviView> create(
            presenter: MviPresenter<V>,
            viewProvider: () -> V
        ): PresenterManager<V> {
            return PresenterManager(presenter, null, viewProvider)
        }
    }
}
