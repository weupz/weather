package weather.feature.weather

import weather.mvi.ViewModel

interface WeatherViewModel : ViewModel<State> {
    fun retry()
}
