package weather.data

import android.content.Context
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.squareup.moshi.JsonReader
import com.squareup.moshi.Moshi
import okio.GzipSource
import okio.Okio

internal class PopulateCities(
    private val context: Context,
    private val moshi: Moshi
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        val source = GzipSource(Okio.source(context.resources.openRawResource(R.raw.cities)))
        val reader = JsonReader.of(Okio.buffer(source))
        val stmt = db.compileStatement(QUERY)
        reader.use {
            val adapter = moshi.adapter(City::class.java)
            reader.beginArray()
            try {
                db.beginTransaction()
                while (reader.hasNext()) {
                    val city = adapter.fromJson(reader) ?: continue
                    stmt.bindLong(1, city.id)
                    stmt.bindString(2, city.name)
                    stmt.bindString(3, city.country)
                    stmt.bindDouble(4, city.coordinate.latitude)
                    stmt.bindDouble(5, city.coordinate.longitude)
                    stmt.executeInsert()
                }
                db.setTransactionSuccessful()
            } finally {
                db.endTransaction()
            }
            reader.endArray()
        }
    }

    private companion object {
        private const val QUERY = "INSERT OR ABORT INTO `City`(`id`,`name`,`country`,`coordinate_latitude`,`coordinate_longitude`) VALUES (?,?,?,?,?)"
    }
}
