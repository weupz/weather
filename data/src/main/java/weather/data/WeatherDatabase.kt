package weather.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [City::class], version = WeatherDatabase.VERSION)
internal abstract class WeatherDatabase : DataComponent, RoomDatabase() {

    companion object {
        const val VERSION = 1
        const val NAME = "weather-db"
    }
}
