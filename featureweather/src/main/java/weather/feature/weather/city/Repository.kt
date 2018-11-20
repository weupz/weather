package weather.feature.weather.city

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.Function
import weather.data.City
import weather.data.CityDao
import weather.scheduler.Schedulers
import javax.inject.Inject

internal class Repository @Inject constructor(
    private val dao: CityDao,
    private val schedulers: Schedulers
) {

    private val mapper = Function { list: List<City> ->
        val data = list.map {
            Suggestion(
                id = it.id,
                title = "${it.name}, ${it.country}",
                description = "${it.coordinate.latitude}, ${it.coordinate.longitude}"
            )
        }
        Event.Success(data)
    }

    fun search(name: String): Flowable<Event> {
        val progress = Single.just(Event.Searching)
        val search = dao.search("%$name%").map(mapper)
        return Single.concat(progress, search)
    }

    fun updateCity(id: Long): Flowable<Event> {
        val updating = Single.just(Event.UpdatingCity)
        val updated = Completable.fromAction { dao.setSelection(id) }
            .subscribeOn(schedulers.io)
            .toSingleDefault(Event.CityUpdated)
        return Single.concat(updating, updated)
    }
}
