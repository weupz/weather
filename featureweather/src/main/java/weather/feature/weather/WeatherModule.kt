package weather.feature.weather

import dagger.Module
import dagger.Provides
import weather.di.Cached
import weather.mvi.lifecycle.PresenterManager

@Module
internal object WeatherModule {

    @Provides @JvmStatic
    fun providesPresenter(@Cached viewModel: WeatherPresenter): PresenterManager {
        return PresenterManager.create(viewModel)
    }
}
