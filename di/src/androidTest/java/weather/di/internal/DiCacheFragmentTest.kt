package weather.di.internal

import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import weather.di.DiCache
import weather.di.DiModule
import weather.di.TestActivity

@RunWith(AndroidJUnit4::class)
class DiCacheFragmentTest {

    @Rule @JvmField
    var activityRule = ActivityTestRule(TestActivity::class.java, true)

    private lateinit var cache: DiCache
    private val key = "@test-cache"

    @Before fun initCache() {
        cache = DiModule.providesCache(activityRule.activity)
        cache.set(key, CacheValue())
    }

    @Test fun testCacheValueReleasedAfterActivityFinished() {
        activityRule.finishActivity()

        val actual = cache.get(key)

        Assert.assertNull(actual)
    }

    @Test fun testAutoClosableCacheValueClosedAfterActivityFinished() {
        val value = cache.get(key) as CacheValue

        activityRule.finishActivity()

        Assert.assertTrue(value.closed)
    }

    @Test fun testCacheValueSameBeforeAndAfterActivityRecreated() {
        val expected = cache.get(key)

        activityRule.runOnUiThread { activityRule.activity.recreate() }

        val actual = cache.get(key)

        Assert.assertSame(expected, actual)
    }

    @Test fun testAutoClosableCacheValueNotClosedAfterActivityRecreated() {
        val value = cache.get(key) as CacheValue

        activityRule.runOnUiThread { activityRule.activity.recreate() }

        Assert.assertFalse(value.closed)
    }

    private class CacheValue : AutoCloseable {

        internal var closed = false

        override fun close() {
            closed = true
        }
    }
}
