package weather.rest.json

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.DateTimeFormatterBuilder

object TextZonedDateTimeJsonAdapter {

    private val formatter = DateTimeFormatterBuilder()
        .parseCaseInsensitive()
        .append(DateTimeFormatter.ISO_LOCAL_DATE)
        .appendLiteral(' ')
        .append(DateTimeFormatter.ISO_LOCAL_TIME)
        .toFormatter()

    @FromJson fun fromJson(text: String?): ZonedDateTime? {
        return text?.let {
            LocalDateTime.parse(it, formatter).atZone(ZoneOffset.UTC)
        }
    }

    @ToJson fun toJson(time: ZonedDateTime?): String? {
        return time?.withZoneSameInstant(ZoneOffset.UTC)?.toLocalDateTime()?.format(formatter)
    }
}
