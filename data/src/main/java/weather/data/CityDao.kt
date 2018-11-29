package weather.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
abstract class CityDao internal constructor(val database: WeatherDatabase) {

    @Query("SELECT * FROM SelectedCity")
    abstract fun selectedCities(): Flowable<List<SelectedCity>>

    @Query("DELETE FROM SelectedCity")
    abstract fun clearCitySelection()

    @Query("INSERT INTO SelectedCity (city_id) VALUES (:cityId)")
    abstract fun insertCitySelection(cityId: Long)

    @Transaction open fun setSelection(cityId: Long) {
        clearCitySelection()
        insertCitySelection(cityId)
    }

    @Query("SELECT * FROM City WHERE name LIKE :name LIMIT 30")
    abstract fun search(name: String): Single<List<City>>

    @Query("SELECT COUNT(id) FROM City")
    abstract fun cityCount(): Single<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertCity(city: City)
}
