package weather.rest.implementation

import retrofit2.Retrofit
import weather.rest.ServiceCreator
import kotlin.reflect.KClass

internal class RetrofitServiceCreator(private val retrofit: Retrofit) : ServiceCreator {

    override fun <T : Any> create(service: KClass<T>): T {
        return retrofit.create(service.java)
    }
}
