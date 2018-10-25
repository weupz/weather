package weather.rest

import weather.rest.service.ForecastService

interface RestComponent {
    val forecastService: ForecastService
}
