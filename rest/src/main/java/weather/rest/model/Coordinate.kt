package weather.rest.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Coordinate(
    @Json(name = "lat")
    val latitude: Double,
    @Json(name = "lon")
    val longitude: Double
)
