package weather.di.internal

import android.os.Bundle
import androidx.collection.SimpleArrayMap
import androidx.fragment.app.Fragment
import weather.di.DiCache

class DiCacheFragment : Fragment(), DiCache {

    private val cache = SimpleArrayMap<String, Any>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        retainInstance = true
    }

    override fun get(key: String): Any? {
        return cache[key]
    }

    override fun set(key: String, value: Any) {
        cache.put(key, value)
    }

    override fun onDestroy() {
        super.onDestroy()
        for (index in 0 until cache.size()) {
            val value = cache.valueAt(index)
            if (value is AutoCloseable) {
                value.close()
            }
        }
    }
}
