package weather.feature.weather

import org.threeten.bp.ZonedDateTime

internal sealed class Event {

    object Refreshing : Event()

    object Loading : Event()

    object Success : Event()

    data class Data(
        val city: String,
        val country: String,
        val time: ZonedDateTime,
        val sunrise: ZonedDateTime,
        val sunset: ZonedDateTime,
        val weather: String,
        val weatherIconUri: String?,
        val temperature: Float
    ) : Event()

    data class Error(val code: Int, val message: String?) : Event()
}
