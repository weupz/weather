package weather.feature.weather

internal sealed class Event {

    object ToggleUnit : Event()

    object Loading : Event()

    data class Success(val data: Data) : Event()

    data class Error(val code: Int, val message: String?) : Event()
}
