package weather.data

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(
    tableName = "City",
    indices = [
        Index("name", "country", name = "city_name_country_index")
    ]
)
@JsonClass(generateAdapter = true)
data class City internal constructor(
    @PrimaryKey
    @ColumnInfo(name = "id")
    @Json(name = "id")
    val id: Long,
    @Json(name = "name")
    @ColumnInfo(name = "name")
    val name: String,
    @Json(name = "country")
    @ColumnInfo(name = "country")
    val country: String,
    @Json(name = "coord")
    @Embedded(prefix = "coordinate_")
    val coordinate: Coordinate
)
