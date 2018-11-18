package weather.data

import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi

fun providesDatabase(context: Context, moshi: Moshi): DataComponent {
    return Room.databaseBuilder(
        context,
        WeatherDatabase::class.java,
        WeatherDatabase.NAME
    )
        .addCallback(PopulateCities(context, moshi))
        .build()
}
