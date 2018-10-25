package weather.rest.service

sealed class CurrentWeatherParams {

    abstract fun toMap(): Map<String, String>

    data class CityName(val city: String, val countryCode: String?) : CurrentWeatherParams() {
        override fun toMap(): Map<String, String> {
            val code = if (countryCode.isNullOrEmpty()) "" else ",$countryCode"
            return mapOf("q" to "$city$code")
        }
    }

    data class Coordinate(val latitude: Double, val longitude: Double) : CurrentWeatherParams() {
        override fun toMap(): Map<String, String> {
            return mapOf("lat" to latitude.toString(), "lon" to longitude.toString())
        }
    }
}
