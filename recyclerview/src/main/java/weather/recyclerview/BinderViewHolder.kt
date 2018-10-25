package weather.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class BinderViewHolder<B>(view: View, val binder: B) : RecyclerView.ViewHolder(view)
