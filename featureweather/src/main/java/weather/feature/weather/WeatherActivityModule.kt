package weather.feature.weather

import androidx.fragment.app.FragmentActivity
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import weather.di.FragmentScope
import weather.feature.weather.city.CityFragment
import weather.feature.weather.city.CityFragmentModule
import weather.feature.weather.city.CityPresenter
import weather.feature.weather.city.CityViewModel

@Module(subcomponents = [WeatherFragmentComponent::class])
internal abstract class WeatherActivityModule {

    @Binds
    @IntoMap
    @ClassKey(WeatherFragment::class)
    abstract fun providesWeatherFragmentInjector(
        impl: WeatherFragmentComponent.Builder
    ): AndroidInjector.Factory<*>

    @FragmentScope
    @ContributesAndroidInjector(modules = [CityFragmentModule::class])
    abstract fun cityFragment(): CityFragment

    @Binds abstract fun providesCityViewModel(impl: CityPresenter): CityViewModel

    @Binds abstract fun providesActivity(activity: WeatherActivity): FragmentActivity
}
