package weather.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Flowable

@Dao
abstract class CityDao {

    @Insert
    abstract fun insert(cities: List<City>)

    @Query("SELECT * FROM City WHERE name LIKE :name AND country LIKE :country LIMIT 10")
    abstract fun search(name: String, country: String): Flowable<List<City>>
}
