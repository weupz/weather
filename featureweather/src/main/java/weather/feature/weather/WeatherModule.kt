package weather.feature.weather

import dagger.Module
import dagger.Provides
import weather.di.DiCache
import weather.di.get
import weather.mvi.asPresenter
import weather.mvi.lifecycle.PresenterManager

@Module
internal object WeatherModule {

    private const val presenter = "weather.feature.weather.PRESENTER"

    @Provides @JvmStatic
    fun providesPresenter(
        cache: DiCache,
        component: WeatherActivityComponent,
        fragment: WeatherFragment
    ): PresenterManager<WeatherView> {
        return PresenterManager.create(
            cache.get(WeatherModule.presenter) { component.presenterCallback().asPresenter() },
            fragment
        )
    }
}
