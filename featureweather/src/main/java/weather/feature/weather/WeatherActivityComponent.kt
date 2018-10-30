package weather.feature.weather

import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import weather.di.ActivityScope
import weather.di.Cached
import weather.di.DiModule
import weather.rest.RestComponent
import weather.scheduler.SchedulersComponent

@ActivityScope
@Component(
    dependencies = [RestComponent::class, SchedulersComponent::class],
    modules = [DiModule::class, WeatherActivityModule::class, AndroidSupportInjectionModule::class]
)
internal interface WeatherActivityComponent {

    fun inject(activity: WeatherActivity)

    fun viewModel(): WeatherPresenter

    @Cached val viewModel: WeatherPresenter

    @Component.Builder
    interface Builder {

        fun rest(component: RestComponent): Builder

        fun schedulers(component: SchedulersComponent): Builder

        @BindsInstance fun activity(activity: WeatherActivity): Builder

        fun build(): WeatherActivityComponent
    }
}
