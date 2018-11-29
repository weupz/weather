package weather.feature.weather.empty

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.weather_icons_typeface_library.WeatherIcons
import weather.feature.weather.R

class EmptyFragment : Fragment() {

    private var menuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menuItem = menu.findItem(R.id.searchCity)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.empty_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<View>(R.id.searchCity).setOnClickListener {
            menuItem?.expandActionView()
        }
        val value = TypedValue().apply {
            view.context.theme.resolveAttribute(R.attr.colorAccent, this, true)
        }
        val icon = IconicsDrawable(view.context, WeatherIcons.Icon.wic_na)
            .sizeDp(72)
            .color(value.data)
        view.findViewById<ImageView>(R.id.emptyIcon).setImageDrawable(icon)
    }
}
