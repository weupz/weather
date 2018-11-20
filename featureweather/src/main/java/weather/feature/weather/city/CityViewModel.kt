package weather.feature.weather.city

import weather.mvi.ViewModel

interface CityViewModel : ViewModel<State> {
    fun search(name: String)
    fun updateCity(id: Long)
}
