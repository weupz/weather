package weather.rest.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CityForecast(
    @Json(name = "city")
    val city: City,
    @Json(name = "list")
    val casts: List<Forecast>
)
