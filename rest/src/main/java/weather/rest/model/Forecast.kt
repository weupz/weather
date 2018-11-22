package weather.rest.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Forecast(
    @Json(name = "main")
    val data: Main,
    @Json(name = "weather")
    val weather: Weather,
    @Json(name = "wind")
    val wind: Wind,
    @Json(name = "clouds")
    val clouds: Clouds
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
        val humidity: Int,
        @Json(name = "sea_level")
        val seaLevel: Float,
        @Json(name = "grnd_level")
        val groundLevel: Float
    )
}
