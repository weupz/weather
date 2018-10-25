package weather.rest.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Forecast(
    val weather: Weather,
    val wind: Wind,
    val clouds: Clouds
)
