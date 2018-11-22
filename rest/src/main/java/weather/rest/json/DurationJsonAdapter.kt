package weather.rest.json

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import org.threeten.bp.Duration

object DurationJsonAdapter : JsonAdapter<Duration>() {

    override fun fromJson(reader: JsonReader): Duration? {
        return when (reader.peek()) {
            JsonReader.Token.NULL -> reader.nextNull()
            else -> Duration.ofMillis(reader.nextLong())
        }
    }

    override fun toJson(writer: JsonWriter, value: Duration?) {
        if (value == null) {
            writer.nullValue()
        } else {
            writer.value(value.toMillis())
        }
    }
}
