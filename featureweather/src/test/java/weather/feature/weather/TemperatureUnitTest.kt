package weather.feature.weather

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class TemperatureUnitTest {

    private val delta = 0.01f

    @Test fun `celsius to fahrenheit`() {
        val given = Temperature(0f, Temperature.Unit.Celsius)
        val expected = 32f
        val actual = given.toFahrenheit().value
        assertEquals(expected, actual, delta)
    }

    @Test fun `celsius to kelvin`() {
        val given = Temperature(0f, Temperature.Unit.Celsius)
        val expected = 273.15f
        val actual = given.toKelvin().value
        assertEquals(expected, actual, delta)
    }

    @Test fun `fahrenheit to celsius`() {
        val given = Temperature(0f, Temperature.Unit.Fahrenheit)
        val expected = -17.7778f
        val actual = given.toCelsius().value
        assertEquals(expected, actual, delta)
    }

    @Test fun `fahrenheit to kelvin`() {
        val given = Temperature(0f, Temperature.Unit.Fahrenheit)
        val expected = 255.372f
        val actual = given.toKelvin().value
        assertEquals(expected, actual, delta)
    }

    @Test fun `kelvin to celsius`() {
        val given = Temperature(0f, Temperature.Unit.Kelvin)
        val expected = -273.14f
        val actual = given.toCelsius().value
        assertEquals(expected, actual, delta)
    }

    @Test fun `kelvin to fahrenheit`() {
        val given = Temperature(0f, Temperature.Unit.Kelvin)
        val expected = -459.67f
        val actual = given.toFahrenheit().value
        assertEquals(expected, actual, delta)
    }
}
