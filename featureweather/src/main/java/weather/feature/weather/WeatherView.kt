package weather.feature.weather

import io.reactivex.Observable
import weather.mvi.MviView

interface WeatherView : MviView {

    fun renderState(state: State)

    fun load(): Observable<Unit>

    fun resetError(): Observable<Unit>
}
