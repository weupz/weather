package weather.feature.weather

import org.junit.Assert.assertEquals
import org.junit.Test

class WindSpeedUnitTest {

    private val delta = 0.01f

    @Test fun `miles to kilometers`() {
        val given = WindSpeed(1f, WindSpeed.Unit.MilesPerHour)
        val expected = 1.60934f
        val actual = given.toKilometersPerHour().value
        assertEquals(expected, actual, delta)
    }

    @Test fun `kilometers to miles`() {
        val given = WindSpeed(1f, WindSpeed.Unit.KilometersPerHour)
        val expected = 0.621371f
        val actual = given.toMilesPerHour().value
        assertEquals(expected, actual, delta)
    }
}
