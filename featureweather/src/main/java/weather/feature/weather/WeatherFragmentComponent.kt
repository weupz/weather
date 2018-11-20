package weather.feature.weather

import dagger.Subcomponent
import dagger.android.AndroidInjector
import weather.di.FragmentScope

@FragmentScope
@Subcomponent(modules = [WeatherFragmentModule::class])
internal interface WeatherFragmentComponent : AndroidInjector<WeatherFragment> {

    fun presenter(): WeatherPresenter

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<WeatherFragment>() {

        abstract override fun build(): WeatherFragmentComponent
    }
}
