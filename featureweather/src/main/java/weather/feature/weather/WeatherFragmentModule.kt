package weather.feature.weather

import dagger.Binds
import dagger.Module
import dagger.Provides
import weather.di.Cached
import weather.di.DiCache
import weather.di.get
import weather.mvi.lifecycle.PresenterManager

@Module(includes = [WeatherFragmentModule.Binder::class])
internal object WeatherFragmentModule {

    private const val presenter = "weather.feature.weather.WEATHER_PRESENTER:%d"

    @Provides @JvmStatic fun providesId(fragment: WeatherFragment): Long {
        return fragment.arguments!!.getLong(WeatherFragment.CITY_ID)
    }

    @Provides @JvmStatic
    fun providesPresenter(@Cached viewModel: WeatherPresenter): PresenterManager {
        return PresenterManager.create(viewModel)
    }

    @Provides @JvmStatic @Cached
    fun providesViewModel(
        cache: DiCache,
        component: WeatherFragmentComponent,
        id: Long
    ): WeatherPresenter {
        return cache.get(presenter.format(id)) { component.presenter() }
    }

    @Module
    abstract class Binder {

        @Binds abstract fun providesViewModel(@Cached impl: WeatherPresenter): WeatherViewModel
    }
}
