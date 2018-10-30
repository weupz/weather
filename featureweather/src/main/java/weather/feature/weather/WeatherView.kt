package weather.feature.weather

import io.reactivex.Observable
import weather.mvi.MviView

interface WeatherView : MviView {

    fun renderState(state: State)

    fun startSync(): Observable<Unit>

    fun stopSync(): Observable<Unit>
}
