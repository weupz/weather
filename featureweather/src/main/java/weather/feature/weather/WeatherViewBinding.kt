package weather.feature.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.transition.TransitionManager
import com.bumptech.glide.signature.ObjectKey
import com.mikepenz.iconics.IconicsDrawable
import org.threeten.bp.ZonedDateTime
import weather.imageloader.GlideApp
import java.text.DecimalFormat

internal class WeatherViewBinding private constructor(private val view: ViewGroup) {

    private val requests = GlideApp.with(view)
    lateinit var viewModel: WeatherViewModel

    private var _state: State? = null

    var state: State?
        get() = _state
        set(value) {
            if (_state?.loading != value?.loading) {
                TransitionManager.beginDelayedTransition(view)
            }
            _state = value
            value?.data?.let { renderData(it, value.loading, value.unitType) }
            this.progressBar = renderProgressBar(this.progressBar, value)
        }

    private val weatherLayout = view.findViewById<View>(R.id.weatherLayout)
    private val city = view.findViewById<TextView>(R.id.city)
    private val country = view.findViewById<TextView>(R.id.country)
    private val temperature = view.findViewById<TextView>(R.id.temperature)
    private val temperatureUnitMetric = view.findViewById<ImageView>(R.id.temperatureUnitMetric)
    private val temperatureUnitImperial = view.findViewById<ImageView>(R.id.temperatureUnitImperial)
    private val weather = view.findViewById<TextView>(R.id.weather)
    private val weatherIcon = view.findViewById<ImageView>(R.id.weatherIcon)
    private val wind = view.findViewById<TextView>(R.id.wind)
    private val pressure = view.findViewById<TextView>(R.id.pressure)
    private val humidity = view.findViewById<TextView>(R.id.humidity)
    private val windUnit = view.findViewById<TextView>(R.id.windUnit)
    private val weatherProgress = view.findViewById<View>(R.id.weatherProgress)
    private var progressBar: View? = null

    init {
        requests.load(R.drawable.city)
            .fitCenter()
            .signature(ObjectKey(view.resources.configuration.orientation))
            .into(view.findViewById(R.id.cityImage))
        val toggle = View.OnClickListener { viewModel.toggleUnitType() }
        temperatureUnitMetric.setOnClickListener(toggle)
        temperatureUnitImperial.setOnClickListener(toggle)
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
        if (progressBar == null) {
            weatherLayout.visibility = View.VISIBLE
        } else {
            weatherLayout.visibility = View.GONE
        }
        return progressBar
    }

    private fun renderData(it: Data, loading: Boolean, unitType: State.UnitType) {
        weatherProgress.visibility = if (loading) View.VISIBLE else View.GONE
        city.text = it.city
        country.text = it.country
        temperature.text = unitType.valueOf(it.temperature).format()
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
        wind.text = unitType.valueOf(it.windSpeed).format()
        pressure.text = it.pressure.format()
        humidity.text = it.humidity.format()
        if (unitType == State.UnitType.Metric) {
            windUnit.setText(R.string.wind_speed_unit_metric)
            temperatureUnitMetric.visibility = View.VISIBLE
            temperatureUnitImperial.visibility = View.GONE
        } else {
            windUnit.setText(R.string.wind_speed_unit_imperial)
            temperatureUnitMetric.visibility = View.GONE
            temperatureUnitImperial.visibility = View.VISIBLE
        }
    }

    companion object {
        private val decimalFormat = DecimalFormat("#.#")
        private fun Number.format(): String = decimalFormat.format(this)

        fun bind(view: ViewGroup): WeatherViewBinding {
            return WeatherViewBinding(view)
        }
    }
}
