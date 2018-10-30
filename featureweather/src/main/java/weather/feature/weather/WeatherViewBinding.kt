package weather.feature.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.signature.ObjectKey
import com.mikepenz.iconics.IconicsDrawable
import org.threeten.bp.ZonedDateTime
import weather.imageloader.GlideApp

internal class WeatherViewBinding private constructor(private val view: ViewGroup) {

    private val requests = GlideApp.with(view)

    private var _state: State? = null

    var state: State?
        get() = _state
        set(value) {
            _state = value
            value?.data?.let { renderData(it) }
            this.progressBar = renderProgressBar(this.progressBar, value)
        }

    private val city = view.findViewById<TextView>(R.id.city)
    private val country = view.findViewById<TextView>(R.id.country)
    private val temperature = view.findViewById<TextView>(R.id.temperature)
    private val weather = view.findViewById<TextView>(R.id.weather)
    private val weatherIcon = view.findViewById<ImageView>(R.id.weatherIcon)
    private var progressBar: View? = null

    init {
        requests.load(R.drawable.city)
            .fitCenter()
            .signature(ObjectKey(view.resources.configuration.orientation))
            .into(view.findViewById(R.id.cityImage))
    }

    private fun renderProgressBar(it: View?, state: State?): View? {
        var progressBar = it
        if (it == null && state?.data == null && state?.loading == true) {
            progressBar = LayoutInflater.from(view.context)
                .inflate(R.layout.weather_view_loading, view, false)
            view.addView(progressBar)
        } else if (progressBar != null) {
            view.removeView(progressBar)
            progressBar = null
        }
        return progressBar
    }

    private fun renderData(it: Data) {
        city.text = it.city
        country.text = it.country
        temperature.text = it.temperature
        weather.text = it.weather
        val now = ZonedDateTime.now()
        val icon = if (now.isAfter(it.sunrise) && now.isBefore(it.sunset)) {
            IconicsDrawable(view.context).icon(it.icon.dayCode)
        } else {
            IconicsDrawable(view.context).icon(it.icon.nightCode)
        }.sizeDp(114)
            .color(ContextCompat.getColor(view.context, R.color.primaryMaterialLight))
        weatherIcon.setImageDrawable(icon)
        weatherIcon.contentDescription = it.weather
    }

    companion object {
        fun bind(view: ViewGroup): WeatherViewBinding {
            return WeatherViewBinding(view)
        }
    }
}
