package weather.feature.weather

import io.reactivex.Flowable
import io.reactivex.Single
import org.threeten.bp.Duration
import org.threeten.bp.ZonedDateTime
import retrofit2.HttpException
import weather.rest.service.CurrentWeatherParams
import weather.rest.service.ForecastService
import weather.scheduler.Schedulers
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.DecimalFormat
import java.util.Locale
import java.util.Random
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

internal class Repository @Inject constructor(
    private val service: ForecastService,
    private val schedulers: Schedulers
) {

    private val random = Random()
    private val temperaturFormat = DecimalFormat("#.#")
    private val params = CurrentWeatherParams.CityName("Jakarta", "id").toMap()

    private fun <T> Single<T>.retryWhenTooManyRequestsOrTimeout() = retryWhen { es ->
        val counter = AtomicInteger()
        es.flatMap {
            if ((it is HttpException && it.code() == TOO_MANY_REQUEST) ||
                it is SocketTimeoutException) {
                val delay = counter.incrementAndGet() * DELAY_MULTIPLIER
                Flowable.timer(
                    delay.toLong(),
                    TimeUnit.MINUTES,
                    schedulers.computation
                )
            } else {
                Flowable.error(it)
            }
        }
    }

    private fun Flowable<Event>.onErrorReturnEvent() = onErrorReturn {
        when (it) {
            is SocketTimeoutException -> Event.Error(2, null)
            is UnknownHostException -> Event.Error(3, null)
            is HttpException -> Event.Error(it.code(), null)
        }
        Event.Error(1, "")
    }

    fun load(): Flowable<Event> {
        val start = Flowable.just(Event.Loading)
        val data = service.cityWeather(params)
            .subscribeOn(schedulers.io)
            .retryWhenTooManyRequestsOrTimeout()
            .flatMapPublisher { (_, name, data, sys, _, weather, _, _, _, date) ->
                val now = ZonedDateTime.now()
                val delay = when {
                    now.isBefore(sys.sunrise) -> Duration.between(now, sys.sunrise)
                    now.isBefore(sys.sunset) -> Duration.between(now, sys.sunset)
                    now.minusHours(2).isBefore(date) -> Duration.ofSeconds(0)
                    else -> {
                        val startOfNextDay = now
                            .toLocalDate()
                            .plusDays(1)
                            .atStartOfDay(now.zone)
                        Duration.between(now, startOfNextDay)
                    }
                }
                val randomDelay = delay.seconds + random.nextInt(MAX_RANDOM)
                val repeater = Flowable.timer(randomDelay, TimeUnit.SECONDS, schedulers.computation)
                    .flatMap { load() }

                val (w) = weather
                val result = Data(
                    city = name,
                    country = Locale("", sys.country).displayCountry,
                    time = date,
                    sunrise = sys.sunrise,
                    sunset = sys.sunset,
                    weather = "${w.main} (${w.description})",
                    icon = IconCodeMapper.code(w.id),
                    temperature = temperaturFormat.format(data.temperature),
                    nextLoad = now.plusSeconds(randomDelay)
                )
                Flowable.merge(Flowable.just(Event.Success(result)), repeater)
            }
            .onErrorReturnEvent()
        return Flowable.concat(start, data)
    }

    private companion object {
        private const val DELAY_MULTIPLIER = 15
        private const val TOO_MANY_REQUEST = 429
        private const val MAX_RANDOM = 30 * 60
    }
}
