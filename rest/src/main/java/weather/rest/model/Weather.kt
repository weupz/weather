package weather.rest.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import weather.rest.json.IconUri

@JsonClass(generateAdapter = true)
data class Weather(
    @Json(name = "id")
    val id: Long,
    @Json(name = "main")
    val main: String,
    @Json(name = "description")
    val description: String,
    @Json(name = "icon") @IconUri
    val iconUri: String?
)
