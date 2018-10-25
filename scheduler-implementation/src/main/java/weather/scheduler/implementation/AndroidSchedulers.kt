package weather.scheduler.implementation

import android.os.Looper
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import weather.scheduler.Schedulers
import java.util.concurrent.Executor

typealias RxSchedulers = io.reactivex.schedulers.Schedulers
typealias RxAndroidSchedulers = io.reactivex.android.schedulers.AndroidSchedulers

internal class AndroidSchedulers(
    private val async: Boolean,
    private val ioExecutor: Executor
) : Schedulers {

    init {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler {
            RxAndroidSchedulers.from(Looper.getMainLooper(), async)
        }
        RxJavaPlugins.setInitIoSchedulerHandler {
            RxSchedulers.from(ioExecutor)
        }
    }

    override val main: Scheduler get() = RxAndroidSchedulers.mainThread()

    override val io: Scheduler get() = RxSchedulers.io()

    override val computation: Scheduler get() = RxSchedulers.computation()
}
