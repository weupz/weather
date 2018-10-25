package weather.scheduler

import io.reactivex.Scheduler

interface Schedulers {
    val main: Scheduler
    val io: Scheduler
    val computation: Scheduler
}
