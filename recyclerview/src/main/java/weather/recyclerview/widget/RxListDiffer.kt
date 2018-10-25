package weather.recyclerview.widget

import androidx.annotation.Keep
import androidx.recyclerview.widget.DiffUtil
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.subjects.PublishSubject

class RxListDiffer<T>(
    updateCallback: ItemsListUpdateCallback<T>,
    config: RxDifferConfig<T>,
    detectMove: Boolean = true
) {

    private val newListSubject = PublishSubject.create<List<T>>()

    private val helper = Helper(updateCallback, config, detectMove)

    @Keep private val disposable: Disposable

    init {
        disposable = newListSubject.toFlowable(BackpressureStrategy.LATEST)
            .flatMap(helper)
            .scan(DiffUtilResult(emptyList()) { it.onItems(emptyList()) }, helper)
            .onBackpressureBuffer()
            .observeOn(config.mainScheduler)
            .subscribe(helper)
    }

    fun submitList(newList: List<T>) {
        newListSubject.onNext(newList)
    }

    private class DiffUtilCallback<T>(
        private val oldList: List<T>,
        private val newList: List<T>,
        private val callback: DiffUtil.ItemCallback<T>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return callback.areItemsTheSame(oldList[oldItemPosition], newList[newItemPosition])
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return callback.areContentsTheSame(oldList[oldItemPosition], newList[newItemPosition])
        }

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            return callback.getChangePayload(oldList[oldItemPosition], newList[newItemPosition])
        }

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size
    }

    private class DiffUtilResult<T>(
        internal val list: List<T>,
        internal val dispatchTo: (ItemsListUpdateCallback<T>) -> Unit
    )

    private class Helper<T>(
        private val updateCallback: ItemsListUpdateCallback<T>,
        private val config: RxDifferConfig<T>,
        private val detectMove: Boolean
    ) :
        Function<List<T>, Flowable<List<T>>>,
        BiFunction<DiffUtilResult<T>, List<T>, DiffUtilResult<T>>,
        Consumer<DiffUtilResult<T>> {

        override fun apply(newList: List<T>): Flowable<List<T>> {
            return Flowable.just(newList).subscribeOn(config.computationScheduler)
        }

        override fun apply(oldResult: DiffUtilResult<T>, newList: List<T>): DiffUtilResult<T> {
            val oldList = oldResult.list
            val dispatchTo: (ItemsListUpdateCallback<T>) -> Unit = when {
                oldList === newList || (oldList.isEmpty() && newList.isEmpty()) -> {
                    // nothing to do
                    {}
                }
                newList.isEmpty() -> {
                    // fast removal
                    {
                        it.onItems(newList)
                        it.onRemoved(0, oldList.size)
                    }
                }
                oldList.isEmpty() -> {
                    // fast insertion
                    {
                        it.onItems(newList)
                        it.onInserted(0, newList.size)
                    }
                }
                else -> {
                    val callback = DiffUtilCallback(oldList, newList, config.callback)
                    DiffUtil.calculateDiff(callback, detectMove)
                        .run {
                            { callback: ItemsListUpdateCallback<T> ->
                                callback.onItems(newList)
                                dispatchUpdatesTo(callback)
                            }
                        }
                }
            }
            return DiffUtilResult(newList, dispatchTo)
        }

        override fun accept(result: DiffUtilResult<T>) {
            result.dispatchTo(updateCallback)
        }
    }
}
