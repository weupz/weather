package weather.feature.weather

import org.threeten.bp.ZonedDateTime

data class State internal constructor(
    val city: String? = null,
    val country: String? = null,
    val weather: String? = null,
    val weatherIconUri: String? = null,
    val temperature: Float? = null,
    val time: ZonedDateTime? = null,
    val refreshing: Boolean = false,
    val loading: Boolean = false,
    val errorCode: Int = 0,
    val errorMessage: String? = null
)
