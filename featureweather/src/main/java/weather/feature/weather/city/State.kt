package weather.feature.weather.city

data class State(
    val inProgress: Boolean = false,
    val data: List<Suggestion> = emptyList()
)
