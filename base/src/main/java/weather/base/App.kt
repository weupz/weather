package weather.base

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.jakewharton.threetenabp.AndroidThreeTen
import com.mikepenz.iconics.Iconics
import com.mikepenz.weather_icons_typeface_library.WeatherIcons
import weather.di.COMPONENT_NAME
import weather.imageloader.ImageLoaderGlideModule

class App : Application() {

    private val component: AppComponent by lazy {
        DaggerAppComponent.builder().context(this).build()
    }

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO)
        AndroidThreeTen.init(this)
        Iconics.init(applicationContext)
        Iconics.registerFont(WeatherIcons())
    }

    override fun getSystemService(name: String?): Any? = when (name) {
        COMPONENT_NAME -> component
        ImageLoaderGlideModule.CALL_FACTORY -> component.imageLoaderCallFactory
        ImageLoaderGlideModule.DEFAULT_OPTIONS -> imageRequestOptions
        else -> {
            @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
            super.getSystemService(name)
        }
    }

    private val imageRequestOptions: RequestOptions
        inline get() {
            return RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .format(DecodeFormat.PREFER_ARGB_8888)
        }
}
