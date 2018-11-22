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
        Index("name", name = "city_name_index")
    ]
)
@JsonClass(generateAdapter = true)
data class City internal constructor(
    @PrimaryKey
    @ColumnInfo(name = "id")
    @Json(name = "id")
    val id: Long,
    @ColumnInfo(name = "name")
    @Json(name = "name")
    val name: String,
    @ColumnInfo(name = "country")
    @Json(name = "country")
    val country: String,
    @Embedded(prefix = "coordinate_")
    @Json(name = "coord")
    val coordinate: Coordinate
)
