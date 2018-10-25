package weather.base.imageloader

import dagger.Module
import dagger.Provides
import okhttp3.Call
import okhttp3.OkHttpClient
import weather.imageloader.ImageLoaderGlideModule
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Provider
import javax.inject.Singleton

@Module
internal object ImageLoaderModule {

    @Provides @Singleton @JvmStatic @Named(ImageLoaderGlideModule.CALL_FACTORY)
    fun providesOkHttpClient(client: OkHttpClient): OkHttpClient {
        return client.newBuilder()
            .readTimeout(1, TimeUnit.MINUTES)
            .addNetworkInterceptor(CacheControlInterceptor)
            .build()
    }

    @Provides @JvmStatic @Named(ImageLoaderGlideModule.CALL_FACTORY)
    fun providesCallFactory(
        @Named(ImageLoaderGlideModule.CALL_FACTORY) client: Provider<OkHttpClient>
    ): Call.Factory {
        return Call.Factory { client.get().newCall(it) }
    }
}
