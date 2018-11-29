package weather.data

import android.content.Context
import com.squareup.moshi.JsonReader
import com.squareup.moshi.Moshi
import io.reactivex.Completable
import io.reactivex.functions.Action
import okio.GzipSource
import okio.Okio
import javax.inject.Inject

class AndroidPopulateCities @Inject constructor(
    private val context: Context,
    private val moshi: Moshi,
    private val dao: CityDao
) : PopulateCitiesManager, Action {

    override fun run() {
        val database = dao.database
        val source = GzipSource(Okio.source(context.resources.openRawResource(R.raw.cities)))
        val reader = JsonReader.of(Okio.buffer(source))
        reader.use {
            val adapter = moshi.adapter(City::class.java)
            reader.beginArray()
            database.beginTransaction()
            try {
                while (reader.hasNext()) {
                    dao.insertCity(adapter.fromJson(reader) ?: continue)
                }
                database.setTransactionSuccessful()
            } finally {
                database.endTransaction()
            }
            reader.endArray()
        }
    }

    override fun populateCities(): Completable {
        return Completable.fromAction(this)
    }
}
