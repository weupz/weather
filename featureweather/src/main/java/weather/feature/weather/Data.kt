package weather.feature.weather

import org.threeten.bp.ZonedDateTime

data class Data internal constructor(
    val city: String,
    val country: String,
    val weather: String,
    val icon: Icon,
    val temperature: Float,
    val time: ZonedDateTime,
    val sunrise: ZonedDateTime,
    val sunset: ZonedDateTime,
    val nextLoad: ZonedDateTime
)
