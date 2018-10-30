package weather.feature.weather

data class State internal constructor(
    val data: Data? = null,
    val loading: Boolean = false,
    val errorCode: Int = 0,
    val errorMessage: String? = null
)
