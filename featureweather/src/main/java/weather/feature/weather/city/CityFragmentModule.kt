package weather.feature.weather.city

import dagger.Module
import dagger.Provides
import weather.mvi.lifecycle.PresenterManager

@Module
internal object CityFragmentModule {

    @Provides @JvmStatic fun providesPresenter(impl: CityPresenter): PresenterManager {
        return PresenterManager.create(impl)
    }
}
