package weather.mvi

fun <V : MviView, S, E> DefaultPresenter.Callback<V, S, E>.asPresenter(): MviPresenter<V> {
    return DefaultPresenter(this)
}
