package weather.scheduler.di

import dagger.Module
import dagger.Provides
import weather.scheduler.Schedulers
import weather.scheduler.implementation.AndroidSchedulers
import weather.scheduler.implementation.Executors
import java.util.concurrent.ExecutorService

@Module
object SchedulersModule {

    @Provides @JvmStatic
    internal fun providesExecutor(): ExecutorService = Executors.io

    @Provides @JvmStatic
    internal fun providesSechdulers(ioExecutor: ExecutorService): Schedulers {
        return AndroidSchedulers(true, ioExecutor)
    }
}
