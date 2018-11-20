package weather.feature.weather.empty

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.transition.TransitionManager
import weather.feature.weather.R

class EmptyFragment : Fragment(), MenuItem.OnActionExpandListener {

    private var menuItem: MenuItem? = null
    private var button: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.searchCity).let {
            it.setOnActionExpandListener(this)
            menuItem = it
            setButtonVisibility()
        }
    }

    override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
        setButtonVisibility()
        return false
    }

    override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
        setButtonVisibility()
        return false
    }

    private fun setButtonVisibility() {
        val visibility = if (menuItem?.isActionViewExpanded == false) {
            View.VISIBLE
        } else {
            View.GONE
        }
        val button = button
        if (button != null && visibility != button.visibility) {
            TransitionManager.beginDelayedTransition(button.parent as ViewGroup)
            button.visibility = visibility
        }
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
    }

    override fun onResume() {
        super.onResume()
        setButtonVisibility()
    }
}
