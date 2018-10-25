package weather.mvi

interface MviPresenter<in V : MviView> {
    fun attachView(view: V)
    fun detachView()
}
