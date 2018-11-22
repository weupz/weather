package weather.feature.weather.city

import android.app.SearchManager
import android.content.Context
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.provider.BaseColumns
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.cursoradapter.widget.CursorAdapter
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection
import io.reactivex.disposables.Disposable
import weather.feature.weather.R
import weather.mvi.lifecycle.PresenterManager
import javax.inject.Inject

class CityFragment : Fragment(), SearchView.OnQueryTextListener {

    @Inject internal lateinit var viewModel: CityViewModel
    @Inject internal fun presenter(manager: PresenterManager) {
        manager.assign(lifecycle)
    }

    private var disposable: Disposable? = null
    private var menuItem: MenuItem? = null
    private var searchView: SearchView? = null
    private var adapter: Adapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
        setHasOptionsMenu(true)
        adapter = Adapter(requireContext()) {
            val c = adapter!!.getItem(it) as Cursor
            viewModel.updateCity(c.getLong(ID_IDX))
            menuItem!!.collapseActionView()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.weather_menu, menu)

        val item = menu.findItem(R.id.searchCity)
        val view = item.actionView as SearchView
        view.suggestionsAdapter = adapter
        view.setOnQueryTextListener(this)
        menuItem = item
        searchView = view
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        viewModel.search(newText ?: "")
        return true
    }

    override fun onStart() {
        super.onStart()
        disposable = viewModel.states
            .map { it.data }
            .distinctUntilChanged()
            .subscribe {
                val cursor = it.fold(MatrixCursor(columns)) { acc, suggestion ->
                    acc.apply {
                        addRow(listOf(suggestion.id, suggestion.title, suggestion.description))
                    }
                }
                adapter!!.changeCursor(cursor)
            }
    }

    override fun onStop() {
        disposable?.dispose()
        disposable = null
        super.onStop()
    }

    private class Adapter(
        context: Context,
        private val callback: (Int) -> Unit
    ) : CursorAdapter(context, null, 0) {

        override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.weather_search_city_item, parent, false)
            view.tag = ViewHolder(view)
            return view
        }

        override fun bindView(view: View, context: Context, cursor: Cursor) {
            val holder = view.tag as ViewHolder
            holder.title.text = cursor.getString(TITLE_IDX)
            holder.description.text = cursor.getString(DESCRIPTION_IDX)
            view.setOnClickListener(Listener(cursor.position, callback))
        }

        private class Listener(
            private val position: Int,
            private val callback: (Int) -> Unit
        ) : View.OnClickListener {
            override fun onClick(v: View?) {
                callback(position)
            }
        }
    }

    private class ViewHolder(view: View) {
        val title: TextView = view.findViewById(R.id.text1)
        val description: TextView = view.findViewById(R.id.text2)
    }

    private companion object {
        const val ID = BaseColumns._ID
        const val ID_IDX = 0
        const val TITLE = SearchManager.SUGGEST_COLUMN_TEXT_1
        const val TITLE_IDX = 1
        const val DESCRIPTION = SearchManager.SUGGEST_COLUMN_TEXT_2
        const val DESCRIPTION_IDX = 2
        @JvmField val columns = arrayOf(ID, TITLE, DESCRIPTION)
    }
}
