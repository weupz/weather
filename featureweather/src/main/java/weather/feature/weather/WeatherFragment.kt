package weather.feature.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection
import io.reactivex.disposables.Disposable
import weather.mvi.lifecycle.PresenterManager
import weather.mvi.subscribe
import javax.inject.Inject

class WeatherFragment : Fragment(), WeatherView {

    private lateinit var binding: WeatherViewBinding
    private var disposable: Disposable? = null

    @Inject internal lateinit var viewModel: WeatherViewModel
    @Inject internal fun presenter(manager: PresenterManager) {
        manager.assign(lifecycle)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.weather_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = WeatherViewBinding.bind(view as ViewGroup)
            .apply { this.viewModel = this@WeatherFragment.viewModel }
    }

    override fun onStart() {
        super.onStart()
        disposable = viewModel.subscribe { binding.state = it }
    }

    override fun onStop() {
        disposable?.dispose()
        disposable = null
        super.onStop()
    }
}
