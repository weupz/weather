package weather.feature.weather

data class WindSpeed(val value: Float, val unit: WindSpeed.Unit) {

    fun toKilometersPerHour() = unit.run { kilometersPerHour() }

    fun toMilesPerHour() = unit.run { milesPerHour() }

    sealed class Unit {
        abstract fun WindSpeed.kilometersPerHour(): WindSpeed
        abstract fun WindSpeed.milesPerHour(): WindSpeed

        object KilometersPerHour : Unit() {
            override fun WindSpeed.kilometersPerHour(): WindSpeed = this

            override fun WindSpeed.milesPerHour(): WindSpeed {
                return WindSpeed(value * 0.621371f, KilometersPerHour)
            }
        }

        object MilesPerHour : Unit() {
            override fun WindSpeed.kilometersPerHour(): WindSpeed {
                return WindSpeed(value * 1.60934f, MilesPerHour)
            }

            override fun WindSpeed.milesPerHour(): WindSpeed = this
        }
    }
}
