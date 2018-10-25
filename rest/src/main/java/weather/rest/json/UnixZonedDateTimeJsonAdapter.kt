package weather.rest.json

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import org.threeten.bp.Instant
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime

object UnixZonedDateTimeJsonAdapter {

    @FromJson @UnixZonedDateTime fun fromJson(unix: Long?): ZonedDateTime? {
        return unix?.let {
            ZonedDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneOffset.UTC)
        }
    }

    @ToJson fun toJson(@UnixZonedDateTime time: ZonedDateTime?): Long? {
        return time?.withZoneSameInstant(ZoneOffset.UTC)?.toInstant()?.toEpochMilli()
    }
}
