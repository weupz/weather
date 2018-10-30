package weather.feature.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import weather.mvi.lifecycle.PresenterManager
import javax.inject.Inject

class WeatherFragment : Fragment(), WeatherView {

    private lateinit var binding: WeatherViewBinding

    @Inject fun presenter(manager: PresenterManager<WeatherView>) {
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
    }

    override fun renderState(state: State) {
        binding.run { this.state = state }
    }

    override fun startSync(): Observable<Unit> = Observable.just(Unit)

    override fun stopSync(): Observable<Unit> = Observable.empty()
}
