package weather.rest.json

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

object IconUriJsonAdapter {
    private const val URI_FORMAT = "https://openweathermap.org/img/w/%s.png"

    @FromJson @IconUri fun fromJson(text: String?): String? {
        return text?.let { URI_FORMAT.format(it) }
    }

    @ToJson fun toJson(@IconUri uri: String?): String? {
        return uri
    }
}
