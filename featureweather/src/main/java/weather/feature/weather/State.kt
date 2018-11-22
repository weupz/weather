package weather.feature.weather

data class State internal constructor(
    val data: Data? = null,
    val unitType: UnitType = UnitType.Metric,
    val loading: Boolean = false,
    val errorCode: Int = 0,
    val errorMessage: String? = null
) {

    sealed class UnitType {
        abstract fun valueOf(temperature: Temperature): Float
        abstract fun valueOf(windSpeed: WindSpeed): Float

        object Metric : UnitType() {
            override fun valueOf(temperature: Temperature): Float {
                return temperature.toCelsius().value
            }

            override fun valueOf(windSpeed: WindSpeed): Float {
                return windSpeed.toKilometersPerHour().value
            }
        }

        object Imperial : UnitType() {
            override fun valueOf(temperature: Temperature): Float {
                return temperature.toFahrenheit().value
            }

            override fun valueOf(windSpeed: WindSpeed): Float {
                return windSpeed.toMilesPerHour().value
            }
        }
    }
}
