package weather.rest.service

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap
import weather.rest.model.CityForecast
import weather.rest.model.CityWeather

interface ForecastService {

    @GET("weather")
    fun cityWeather(@QueryMap params: Map<String, String>): Single<CityWeather>

    @GET("forecast")
    fun cityForecast(@Query("q") query: String): Single<CityForecast>
}
