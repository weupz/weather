package weather.feature.weather

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import weather.data.CityDao
import weather.data.SelectedCity
import weather.di.component
import weather.feature.weather.city.CityFragment
import weather.feature.weather.empty.EmptyFragment
import weather.scheduler.Schedulers
import javax.inject.Inject

class WeatherActivity : AppCompatActivity(),
    HasSupportFragmentInjector,
    Consumer<List<SelectedCity>> {

    @Inject internal lateinit var injector: DispatchingAndroidInjector<Fragment>

    @Inject internal lateinit var schedulers: Schedulers
    @Inject internal lateinit var dao: CityDao

    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Weather)
        super.onCreate(savedInstanceState)
        supportFragmentInjector()
        setContentView(R.layout.weather_activity)
        setSupportActionBar(findViewById(R.id.toolbar))
        if (savedInstanceState === null) {
            supportFragmentManager.beginTransaction()
                .add(CityFragment(), "search-city")
                .commit()
        }
        initDisposable()
    }

    private fun initDisposable() {
        disposable = dao.selectedCities()
            .distinctUntilChanged { a, b -> a.firstOrNull()?.cityId == b.firstOrNull()?.cityId }
            .observeOn(schedulers.main)
            .subscribe(this)
    }

    override fun onStart() {
        super.onStart()
        if (disposable == null) initDisposable()
    }

    override fun accept(t: List<SelectedCity>) {
        val fragment = supportFragmentManager.findFragmentById(R.id.contentFrame)
        val id = t.firstOrNull()?.cityId
        if (id == null && fragment !is EmptyFragment) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.contentFrame, EmptyFragment())
                .commit()
        } else if (id !== null && (fragment !is WeatherFragment
                || fragment.arguments?.getLong(WeatherFragment.CITY_ID) != id)) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.contentFrame, WeatherFragment.newInstance(id))
                .commit()
        }
    }

    override fun onStop() {
        disposable?.dispose()
        disposable = null
        super.onStop()
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        if (!::injector.isInitialized) {
            synchronized(this) {
                if (!::injector.isInitialized) {
                    DaggerWeatherActivityComponent.builder()
                        .rest(component())
                        .data(component())
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
