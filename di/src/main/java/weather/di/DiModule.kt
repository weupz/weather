package weather.di

import android.app.Activity
import androidx.collection.SimpleArrayMap
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.GenericLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import dagger.Module
import dagger.Provides
import weather.di.internal.DiCacheFragment

@Module
object DiModule {
    private const val TAG = "@app:di-cache"
    private val caches = SimpleArrayMap<Activity, DiCache>()

    private fun FragmentActivity.initCache(): DiCache {
        var cache = supportFragmentManager.findFragmentByTag(TAG) as? DiCache
        if (cache == null) {
            cache = DiCacheFragment()
            supportFragmentManager.beginTransaction()
                .add(cache, TAG)
                .commitAllowingStateLoss()
        }
        return cache
    }

    @Provides @JvmStatic
    internal fun providesCache(activity: FragmentActivity): DiCache {
        var cache = caches[activity]
        if (cache == null) {
            synchronized(this) {
                cache = caches[activity]
                if (cache == null) {
                    cache = activity.initCache()
                    caches.put(activity, cache)
                    activity.lifecycle.addObserver(CacheLifecycleObserver(caches, activity))
                }
            }
        }
        return cache!!
    }

    private class CacheLifecycleObserver(
        private val caches: SimpleArrayMap<Activity, DiCache>,
        private val activity: Activity
    ) : GenericLifecycleObserver {
        override fun onStateChanged(source: LifecycleOwner?, event: Lifecycle.Event?) {
            if (event == Lifecycle.Event.ON_DESTROY) {
                caches.remove(activity)
            }
        }
    }
}
