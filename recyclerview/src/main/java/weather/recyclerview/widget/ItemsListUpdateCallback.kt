package weather.recyclerview.widget

import androidx.recyclerview.widget.ListUpdateCallback

interface ItemsListUpdateCallback<T> : ListUpdateCallback {

    fun onItems(items: List<T>)
}
