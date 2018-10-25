package weather.imageloader

import android.annotation.SuppressLint
import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.Excludes
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpLibraryGlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import okhttp3.Call
import java.io.InputStream

@GlideModule
@Excludes(OkHttpLibraryGlideModule::class)
class ImageLoaderGlideModule : AppGlideModule() {

    @SuppressLint("WrongConstant")
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        (context.applicationContext.getSystemService(DEFAULT_OPTIONS) as RequestOptions).let {
            builder.setDefaultRequestOptions(it)
        }
    }

    override fun isManifestParsingEnabled() = false

    @SuppressLint("WrongConstant")
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        (context.applicationContext.getSystemService(CALL_FACTORY) as Call.Factory).let {
            val factory = OkHttpUrlLoader.Factory(it)
            registry.replace(GlideUrl::class.java, InputStream::class.java, factory)
        }
    }

    companion object {
        const val CALL_FACTORY = "weather.imageloader.ImageLoaderGlideModule.CALL_FACTORY"
        const val DEFAULT_OPTIONS = "weather.imageloader.ImageLoaderGlideModule.DEFAULT_OPTIONS"
    }
}
