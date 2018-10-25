package weather.feature.weather

import androidx.fragment.app.FragmentActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import weather.di.FragmentScope

@Module
abstract class WeatherActivityModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = [WeatherModule::class])
    abstract fun weatherFragment(): WeatherFragment

    @Binds
    abstract fun providesActivity(activity: WeatherActivity): FragmentActivity
}
