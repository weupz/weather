package weather.rest.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.threeten.bp.ZonedDateTime
import weather.rest.json.UnixZonedDateTime

@JsonClass(generateAdapter = true)
data class CityWeather(
    @Json(name = "id")
    val id: Long,
    @Json(name = "name")
    val name: String,
    @Json(name = "main")
    val main: Main,
    @Json(name = "sys")
    val sys: Sys,
    @Json(name = "coord")
    val coordinate: Coordinate,
    @Json(name = "weather")
    val weather: List<Weather>,
    @Json(name = "clouds")
    val clouds: Clouds,
    @Json(name = "visibility")
    val visibility: Int,
    @Json(name = "wind")
    val wind: Wind,
    @Json(name = "dt") @UnixZonedDateTime
    val date: ZonedDateTime
) {

    @JsonClass(generateAdapter = true)
    data class Main(
        @Json(name = "temp")
        val temperature: Float,
        @Json(name = "temp_min")
        val temperatureMin: Float,
        @Json(name = "temp_max")
        val temperatureMax: Float,
        @Json(name = "pressure")
        val pressure: Float,
        @Json(name = "humidity")
        val humidity: Int
    )

    @JsonClass(generateAdapter = true)
    data class Sys(
        @Json(name = "country")
        val country: String,
        @Json(name = "sunrise") @UnixZonedDateTime
        val sunrise: ZonedDateTime,
        @Json(name = "sunset") @UnixZonedDateTime
        val sunset: ZonedDateTime
    )
}
