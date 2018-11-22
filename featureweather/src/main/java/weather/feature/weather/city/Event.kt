package weather.feature.weather.city

sealed class Event {

    object Searching : Event()

    data class Success(val data: List<Suggestion>) : Event()

    object UpdatingCity : Event()

    object CityUpdated : Event()
}
