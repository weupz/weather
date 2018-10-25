package weather.recyclerview

import com.hannesdorfmann.adapterdelegates4.AbsDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager
import weather.recyclerview.widget.ItemsListUpdateCallback
import weather.recyclerview.widget.RxDifferConfig
import weather.recyclerview.widget.RxListDiffer

class ListDiffAdapter<T>(
    manager: AdapterDelegatesManager<List<T>>,
    config: RxDifferConfig<T>,
    detectMove: Boolean
) : AbsDelegationAdapter<List<T>>(manager) {

    private val differ = RxListDiffer(UpdateCallback(this), config, detectMove)

    fun submitList(newList: List<T>) = differ.submitList(newList)

    override fun getItemCount(): Int = items.size

    private class UpdateCallback<T>(
        private val adapter: ListDiffAdapter<T>
    ) : ItemsListUpdateCallback<T> {

        override fun onItems(items: List<T>) {
            adapter.items = items
        }

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            adapter.notifyItemRangeChanged(position, count, payload)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            adapter.notifyItemMoved(fromPosition, toPosition)
        }

        override fun onInserted(position: Int, count: Int) {
            adapter.notifyItemRangeInserted(position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            adapter.notifyItemRangeRemoved(position, count)
        }
    }
}
