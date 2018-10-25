package weather.base

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import okhttp3.Call
import weather.base.imageloader.ImageLoaderModule
import weather.imageloader.ImageLoaderGlideModule
import weather.rest.RestComponent
import weather.rest.di.RestModule
import weather.scheduler.SchedulersComponent
import weather.scheduler.di.SchedulersModule
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        ImageLoaderModule::class,
        RestModule::class,
        SchedulersModule::class
    ]
)
internal interface AppComponent : RestComponent, SchedulersComponent {

    @get:Named(ImageLoaderGlideModule.CALL_FACTORY)
    val imageLoaderCallFactory: Call.Factory

    @Component.Builder
    interface Builder {

        @BindsInstance fun context(context: Context): Builder

        fun build(): AppComponent
    }
}
