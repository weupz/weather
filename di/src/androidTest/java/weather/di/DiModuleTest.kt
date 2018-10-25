package weather.di

import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import weather.di.internal.DiCacheFragment

@RunWith(AndroidJUnit4::class)
class DiModuleTest {

    @Rule @JvmField
    var activityRule = ActivityTestRule(TestActivity::class.java, true)

    @Test fun testProvidedDiCacheTypeIsCorrect() {
        val expected = DiCacheFragment::class

        val actual = DiModule.providesCache(activityRule.activity)::class

        Assert.assertEquals(expected, actual)
    }

    @Test fun testDiCacheIsSameAfterActivityRecreated() {
        val expected = DiModule.providesCache(activityRule.activity)

        activityRule.runOnUiThread { activityRule.activity.recreate() }

        val actual = DiModule.providesCache(activityRule.activity)

        Assert.assertSame(expected, actual)
    }
}
