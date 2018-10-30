package weather.feature.weather

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import weather.di.component
import javax.inject.Inject

class WeatherActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject internal lateinit var injector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Weather)
        super.onCreate(savedInstanceState)
        supportFragmentInjector()
        setContentView(R.layout.weather_activity)
        setSupportActionBar(findViewById(R.id.toolbar))
        if (savedInstanceState === null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.contentFrame, WeatherFragment())
                .commit()
        }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        if (!::injector.isInitialized) {
            synchronized(this) {
                if (!::injector.isInitialized) {
                    DaggerWeatherActivityComponent.builder()
                        .rest(component())
                        .schedulers(component())
                        .activity(this)
                        .build()
                        .inject(this)
                }
            }
        }
        return injector
    }
}
