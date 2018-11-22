package weather.feature.weather

data class Icon internal constructor(
    private val code: String,
    private val day: String = "",
    private val night: String = ""
) {

    val dayCode: String get() = "wic-$day$code"

    val nightCode: String get() = "wic-$night$code"
}
