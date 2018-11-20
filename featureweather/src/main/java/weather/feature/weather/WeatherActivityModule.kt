package weather.feature.weather

import androidx.fragment.app.FragmentActivity
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import weather.di.Cached
import weather.di.DiCache
import weather.di.FragmentScope
import weather.di.get
import weather.feature.weather.city.CityFragment
import weather.feature.weather.city.CityFragmentModule
import weather.feature.weather.city.CityPresenter
import weather.feature.weather.city.CityViewModel
import weather.mvi.ViewModel

@Module(includes = [WeatherActivityModule.Binder::class])
internal object WeatherActivityModule {

    private const val presenter = "weather.feature.weather.PRESENTER"

    @Provides @JvmStatic @Cached fun providesViewModel(
        cache: DiCache,
        component: WeatherActivityComponent
    ): WeatherPresenter {
        return cache.get(presenter) { component.weatherViewModel() }
    }

    @Module
    abstract class Binder {

        @FragmentScope
        @ContributesAndroidInjector(modules = [WeatherModule::class])
        abstract fun weatherFragment(): WeatherFragment

        @Binds
        abstract fun providesWeatherViewModel(@Cached impl: WeatherPresenter): WeatherViewModel

        @Binds abstract fun providesViewModel(impl: WeatherViewModel): ViewModel<State>

        @FragmentScope
        @ContributesAndroidInjector(modules = [CityFragmentModule::class])
        abstract fun cityFragment(): CityFragment

        @Binds abstract fun providesCityViewModel(impl: CityPresenter): CityViewModel

        @Binds abstract fun providesActivity(activity: WeatherActivity): FragmentActivity
    }
}
