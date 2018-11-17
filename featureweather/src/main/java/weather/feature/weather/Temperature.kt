package weather.feature.weather

data class Temperature(val value: Float, val unit: Temperature.Unit) {

    fun toFahrenheit() = unit.run { fahrenheit() }

    fun toKelvin() = unit.run { kelvin() }

    fun toCelsius() = unit.run { celsius() }

    sealed class Unit {
        internal abstract fun Temperature.fahrenheit(): Temperature
        internal abstract fun Temperature.kelvin(): Temperature
        internal abstract fun Temperature.celsius(): Temperature

        object Kelvin : Unit() {
            override fun Temperature.fahrenheit(): Temperature {
                return Temperature((value - 273.15f) * 9 / 5 + 32, Kelvin)
            }

            override fun Temperature.kelvin(): Temperature = this

            override fun Temperature.celsius(): Temperature {
                return Temperature(value - 273.15f, Kelvin)
            }
        }

        object Fahrenheit : Unit() {
            override fun Temperature.fahrenheit(): Temperature = this

            override fun Temperature.kelvin(): Temperature {
                return Temperature(((value - 32) * 5 / 9) + 273.15f, Fahrenheit)
            }

            override fun Temperature.celsius(): Temperature {
                return Temperature((value - 32) * 5 / 9, Fahrenheit)
            }
        }

        object Celsius : Unit() {
            override fun Temperature.celsius(): Temperature = this

            override fun Temperature.kelvin(): Temperature {
                return Temperature(value + 273.15f, Celsius)
            }

            override fun Temperature.fahrenheit(): Temperature {
                return Temperature(value * 9 / 5 + 32, Celsius)
            }
        }
    }
}
