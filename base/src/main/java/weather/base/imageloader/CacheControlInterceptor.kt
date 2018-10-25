package weather.base.imageloader

import okhttp3.Interceptor
import okhttp3.Response

object CacheControlInterceptor : Interceptor {

    private const val eTagKey = "ETag"
    private const val lastModifiedKey = "Last-Modified"
    private const val cacheControlKey = "Cache-Control"
    private const val cacheControlValue = "max-age=31536000"

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val headers = response.headers()
        val cacheControl = headers[cacheControlKey]
        return if (cacheControl?.contains("max-age", true) == true ||
            (headers[eTagKey].isNullOrEmpty() && headers[lastModifiedKey].isNullOrEmpty())) {
            response
        } else {
            response.newBuilder()
                .headers(headers.newBuilder().set(cacheControlKey, cacheControlValue).build())
                .build()
        }
    }
}
