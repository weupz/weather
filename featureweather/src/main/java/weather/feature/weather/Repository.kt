package weather.feature.weather

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import retrofit2.HttpException
import weather.rest.model.CityWeather
import weather.rest.service.CurrentWeatherParams
import weather.rest.service.ForecastService
import weather.scheduler.Schedulers
import java.net.SocketTimeoutException
import java.util.Locale
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

internal class Repository @Inject constructor(
    private val service: ForecastService,
    private val schedulers: Schedulers
) {

    private val subject = BehaviorSubject.create<Event>()

    private val params = CurrentWeatherParams.CityName("Jakarta", "id")
        .toMap()

    private fun Single<CityWeather>.saveOnSuccess(): Single<CityWeather> {
        return doOnSuccess {
            val weather = it.weather.first()
            val result = Event.Data(
                city = it.name,
                country = Locale("", it.sys.country).displayCountry,
                time = it.date,
                sunrise = it.sys.sunrise,
                sunset = it.sys.sunset,
                weather = "${weather.main} (${weather.description})",
                weatherIconUri = weather.iconUri,
                temperature = it.main.temperature
            )
            subject.onNext(result)
        }
    }

    private fun <T> Single<T>.retryWhenTooManyRequestsOrTimeout(): Single<T> {
        return retryWhen { es ->
            val counter = AtomicInteger()
            es.flatMap {
                if ((it is HttpException && it.code() == TOO_MANY_REQUEST) ||
                    it is SocketTimeoutException) {
                    val delay = counter.incrementAndGet() * DELAY_MULTIPLIER
                    Flowable.timer(
                        delay.toLong(),
                        TimeUnit.SECONDS,
                        schedulers.computation
                    )
                } else {
                    Flowable.error(it)
                }
            }
        }
    }

    fun load(): Flowable<Event> {
        val start = Single.just(Event.Loading)
        val data = service.cityWeather(params)
            .subscribeOn(schedulers.io)
            .retryWhenTooManyRequestsOrTimeout()
            .saveOnSuccess()
            .map<Event> { Event.Success }
            .onErrorReturn { Event.Error(1, "") }
        return Single.concat(start, data)
    }

    fun data(): Flowable<Event> = subject.toFlowable(BackpressureStrategy.LATEST)

    private companion object {
        private const val DELAY_MULTIPLIER = 15
        private const val TOO_MANY_REQUEST = 429
    }
}
