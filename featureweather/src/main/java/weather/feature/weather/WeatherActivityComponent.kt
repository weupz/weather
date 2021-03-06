package weather.feature.weather

import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import weather.data.DataComponent
import weather.di.ActivityScope
import weather.di.DiModule
import weather.rest.RestComponent
import weather.scheduler.SchedulersComponent

@ActivityScope
@Component(
    dependencies = [RestComponent::class, SchedulersComponent::class, DataComponent::class],
    modules = [DiModule::class, WeatherActivityModule::class, AndroidSupportInjectionModule::class]
)
internal interface WeatherActivityComponent {

    fun inject(activity: WeatherActivity)

    @Component.Builder
    interface Builder {

        fun rest(component: RestComponent): Builder

        fun schedulers(component: SchedulersComponent): Builder

        fun data(component: DataComponent): Builder

        @BindsInstance fun activity(activity: WeatherActivity): Builder

        fun build(): WeatherActivityComponent
    }
}
