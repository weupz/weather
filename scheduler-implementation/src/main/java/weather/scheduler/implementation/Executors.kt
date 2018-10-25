package weather.scheduler.implementation

import android.os.AsyncTask
import java.util.concurrent.ExecutorService
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

internal object Executors {

    val io: ExecutorService by lazy {
        val executor = AsyncTask.THREAD_POOL_EXECUTOR
        executor as? ExecutorService ?: createExecutor()
    }

    private fun createExecutor(): ExecutorService {
        val cpuCount = Runtime.getRuntime().availableProcessors()
        // We want at least 2 threads and at most 4 threads in the core pool,
        // preferring to have 1 less than the CPU count to avoid saturating
        // the CPU with background work
        val corePoolSize = Math.max(2, Math.min(cpuCount - 1, 4))
        val maximumPoolSize = cpuCount * 2 + 1
        val keepAliveTime = 30L
        return ThreadPoolExecutor(
            corePoolSize,
            maximumPoolSize,
            keepAliveTime,
            TimeUnit.SECONDS,
            LinkedBlockingQueue(128),
            Factory()
        )
    }

    private class Factory : ThreadFactory {
        private val number = AtomicInteger()

        override fun newThread(r: Runnable?): Thread {
            return Thread(r, "IO Task #${number.getAndIncrement()}").apply {
                if (!isDaemon) {
                    isDaemon = false
                }
            }
        }
    }
}
