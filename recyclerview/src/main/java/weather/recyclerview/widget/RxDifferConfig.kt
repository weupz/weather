package weather.recyclerview.widget

import androidx.recyclerview.widget.DiffUtil
import io.reactivex.Scheduler

data class RxDifferConfig<T>(
    val mainScheduler: Scheduler,
    val computationScheduler: Scheduler,
    val callback: DiffUtil.ItemCallback<T>
)
