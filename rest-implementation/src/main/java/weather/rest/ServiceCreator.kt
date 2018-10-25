package weather.rest

import kotlin.reflect.KClass

interface ServiceCreator {

    /**
     * Create new service instance defined by [service] interface
     */
    fun <T : Any> create(service: KClass<T>): T
}

internal inline fun <reified T : Any> ServiceCreator.create(): T = create(T::class)
