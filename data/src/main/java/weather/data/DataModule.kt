package weather.data

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module

@Module
abstract class DataModule {

    @Binds
    abstract fun bindsPopulateCitiesManager(impl: AndroidPopulateCities): PopulateCitiesManager
}

fun providesDatabase(context: Context): DatabaseComponent {
    return Room.databaseBuilder(
        context,
        WeatherDatabase::class.java,
        WeatherDatabase.NAME
    ).build()
}
