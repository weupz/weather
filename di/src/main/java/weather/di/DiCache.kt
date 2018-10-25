package weather.di

interface DiCache {
    fun get(key: String): Any?
    fun set(key: String, value: Any)
}

inline fun <reified T> DiCache.get(key: String, creator: () -> T): T {
    var value = get(key)
    if (value == null) {
        synchronized(this) {
            value = get(key)
            if (value == null) {
                value = creator()
                set(key, value!!)
            }
        }
    }
    return value as T
}
