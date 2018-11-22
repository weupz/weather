package weather.data

import androidx.room.ColumnInfo
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Coordinate internal constructor(
    @ColumnInfo(name = "latitude")
    @Json(name = "lat")
    val latitude: Double,
    @ColumnInfo(name = "longitude")
    @Json(name = "lon")
    val longitude: Double
)
