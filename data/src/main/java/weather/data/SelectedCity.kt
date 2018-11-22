package weather.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "SelectedCity",
    foreignKeys = [
        ForeignKey(
            entity = City::class,
            parentColumns = ["id"],
            childColumns = ["city_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("city_id", name = "selected_city_id_index", unique = true)
    ]
)
data class SelectedCity internal constructor(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "city_id")
    val cityId: Long
)
