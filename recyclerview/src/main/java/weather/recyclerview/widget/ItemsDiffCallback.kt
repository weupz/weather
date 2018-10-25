package weather.recyclerview.widget

import androidx.recyclerview.widget.DiffUtil

class ItemsDiffCallback(
    private val callbacks: Iterable<DiffUtil.ItemCallback<Any>>
) : DiffUtil.ItemCallback<Any>() {

    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return callbacks.firstOrNull { it.areItemsTheSame(oldItem, newItem) } != null
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return callbacks.firstOrNull { it.areContentsTheSame(oldItem, newItem) } != null
    }

    override fun getChangePayload(oldItem: Any, newItem: Any): Any? {
        callbacks.forEach {
            val payload = it.getChangePayload(oldItem, newItem)
            if (payload != null) {
                return payload
            }
        }
        return null
    }
}
