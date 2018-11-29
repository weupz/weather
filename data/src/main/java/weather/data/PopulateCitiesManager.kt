package weather.data

import io.reactivex.Completable

interface PopulateCitiesManager {
    fun populateCities(): Completable
}
