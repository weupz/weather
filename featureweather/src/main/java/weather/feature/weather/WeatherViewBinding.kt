package weather.feature.weather

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import weather.imageloader.GlideApp

class WeatherViewBinding private constructor(view: View) {

    private val requests = GlideApp.with(view)

    private var _state: State? = null

    var state: State?
        get() = _state
        set(value) {
            _state = value
            value?.let {
                city.text = it.city
                country.text = it.country
                temperature.text = it.temperature?.toString()
                weather.text = it.weather
                requests.load(it.weatherIconUri).fitCenter().into(weatherIcon)
                weatherIcon.contentDescription = it.weather
            }
        }

    private val city = view.findViewById<TextView>(R.id.city)
    private val country = view.findViewById<TextView>(R.id.country)
    private val temperature = view.findViewById<TextView>(R.id.temperature)
    private val weather = view.findViewById<TextView>(R.id.weather)
    private val weatherIcon = view.findViewById<ImageView>(R.id.weatherIcon)

    companion object {
        fun bind(view: View): WeatherViewBinding {
            return WeatherViewBinding(view)
        }
    }
}
