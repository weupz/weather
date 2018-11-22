package weather.feature.weather

import org.threeten.bp.ZonedDateTime

data class Data internal constructor(
    val city: String,
    val country: String,
    val weather: String,
    val icon: Icon,
    val temperature: Temperature,
    val time: ZonedDateTime,
    val sunrise: ZonedDateTime,
    val sunset: ZonedDateTime,
    val humidity: Float,
    val windSpeed: WindSpeed,
    val pressure: Float,
    val nextLoad: ZonedDateTime
)
