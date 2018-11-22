package weather.base

import android.content.Context
import com.squareup.moshi.Moshi
import dagger.BindsInstance
import dagger.Component
import okhttp3.Call
import weather.base.imageloader.ImageLoaderModule
import weather.data.DataComponent
import weather.imageloader.ImageLoaderGlideModule
import weather.rest.RestComponent
import weather.rest.di.RestModule
import weather.scheduler.SchedulersComponent
import weather.scheduler.di.SchedulersModule
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [DataComponent::class],
    modules = [
        AppModule::class,
        ImageLoaderModule::class,
        RestModule::class,
        SchedulersModule::class
    ]
)
internal interface AppComponent : RestComponent, SchedulersComponent, DataComponent {

    @get:Named(ImageLoaderGlideModule.CALL_FACTORY)
    val imageLoaderCallFactory: Call.Factory

    @Component.Builder
    interface Builder {

        fun dataComponent(component: DataComponent): Builder

        @BindsInstance fun context(context: Context): Builder

        @BindsInstance fun moshi(moshi: Moshi): Builder

        fun build(): AppComponent
    }
}
