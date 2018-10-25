package weather.rest.di

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import weather.rest.ServiceCreator
import weather.rest.create
import weather.rest.implementation.BuildConfig
import weather.rest.implementation.RetrofitServiceCreator
import weather.rest.json.IconUriJsonAdapter
import weather.rest.json.TextZonedDateTimeJsonAdapter
import weather.rest.json.UnixZonedDateTimeJsonAdapter
import weather.rest.service.ForecastService
import java.util.Locale
import javax.inject.Singleton

@Module
object RestModule {

    @Provides @JvmStatic @Singleton
    internal fun providesServiceCreator(
        client: OkHttpClient,
        baseUrl: HttpUrl
    ): ServiceCreator {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .callFactory(client.newBuilder().addNetworkInterceptor(ParamsInterceptor()).build())
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder()
                        .add(UnixZonedDateTimeJsonAdapter)
                        .add(TextZonedDateTimeJsonAdapter)
                        .add(IconUriJsonAdapter)
                        .build()
                )
            )
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
            .build()
        return RetrofitServiceCreator(retrofit)
    }

    @Provides @JvmStatic @Singleton
    internal fun providesForecastService(creator: ServiceCreator): ForecastService {
        return creator.create()
    }

    private class ParamsInterceptor : Interceptor {

        private val appIdName = "appid"
        private val appIdValue = BuildConfig.OPEN_WEATHER_MAP_APP_ID
        private val unitsName = "units"
        private val unitsValue = "metric"
        private val langName = "lang"

        override fun intercept(chain: Interceptor.Chain): Response {
            val url = chain.request().url().newBuilder()
                .addQueryParameter(appIdName, appIdValue)
                .addQueryParameter(unitsName, unitsValue)
                .addQueryParameter(langName, Locale.getDefault().language)
                .build()
            return chain.proceed(chain.request().newBuilder().url(url).build())
        }
    }
}
