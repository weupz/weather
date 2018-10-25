package weather.base

import android.content.Context
import android.os.StatFs
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Dispatcher
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import java.io.File
import java.util.concurrent.ExecutorService
import javax.inject.Singleton
import kotlin.math.max
import kotlin.math.min

@Module
internal object AppModule {

    private const val maxDiskCacheSize = 200 * 1024 * 1024
    private const val minDiskCacheSize = 5 * 1024 * 1024
    private const val diskSizeDivider = 50

    @JvmStatic private fun computeCacheSize(dir: File): Long {
        val statFs = StatFs(dir.absolutePath)
        val available: Long = statFs.blockCountLong * statFs.blockSizeLong
        val size = available / diskSizeDivider
        return max(min(size, maxDiskCacheSize.toLong()), minDiskCacheSize.toLong())
    }

    @JvmStatic private fun providesCache(context: Context): Cache {
        val cacheDir = File(context.cacheDir, "ok-http-cache")
        if (!cacheDir.exists()) cacheDir.mkdirs()
        return Cache(cacheDir, computeCacheSize(cacheDir))
    }

    @Provides @JvmStatic @Singleton
    fun providesOkHttpClient(context: Context, executor: ExecutorService): OkHttpClient {
        return OkHttpClient.Builder()
            .dispatcher(Dispatcher(executor))
            .cache(providesCache(context))
            .build()
    }

    @Provides @JvmStatic
    fun providesBaseUrl(): HttpUrl = HttpUrl.get("https://api.openweathermap.org/data/2.5/")
}
