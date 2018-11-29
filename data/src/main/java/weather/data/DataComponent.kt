package weather.data

interface DataComponent : DatabaseComponent {

    val populateCitiesManager: PopulateCitiesManager
}
