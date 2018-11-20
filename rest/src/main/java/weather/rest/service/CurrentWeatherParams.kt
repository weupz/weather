package weather.rest.service

sealed class CurrentWeatherParams {

    abstract fun toMap(): Map<String, String>

    data class CityId(private val id: Long) : CurrentWeatherParams() {
        override fun toMap(): Map<String, String> {
            return mapOf("id" to "$id")
        }
    }

    data class CityName(
        private val city: String,
        private val countryCode: String?
    ) : CurrentWeatherParams() {
        override fun toMap(): Map<String, String> {
            val code = if (countryCode.isNullOrEmpty()) "" else ",$countryCode"
            return mapOf("q" to "$city$code")
        }
    }

    data class Coordinate(
        private val latitude: Double,
        private val longitude: Double
    ) : CurrentWeatherParams() {
        override fun toMap(): Map<String, String> {
            return mapOf("lat" to latitude.toString(), "lon" to longitude.toString())
        }
    }
}
