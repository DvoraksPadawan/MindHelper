package pac.underpackage.brainhelper

import Databases.TasksDbHelper
import Databases.ToDoListsDbHelper
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pac.underpackage.brainhelper.Adapters.ToDoListsAdapter
import pac.underpackage.brainhelper.Models.ToDoListModel
//import com.example.brainhelper.R
//import com.example.brainhelper.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AActivity(), Connector {
    private lateinit var fabAdd : FloatingActionButton
    private lateinit var rvToDoLists : RecyclerView
    //TODO: tableName muze byt template
    private lateinit var dbTable : ToDoListsDbHelper
    private lateinit var dbTableN : TasksDbHelper
    private var template : Boolean = false
    private lateinit var rvAdapter: ToDoListsAdapter
    private lateinit var sListsTemplates : Switch
    private lateinit var toolbar : androidx.appcompat.widget.Toolbar
    private lateinit var tvHeader : TextView
    private lateinit var etSearch : CustomEditText
    private var searchMode : Boolean = false
    private lateinit var searchTextWatcher: SearchTextWatcher
    private var deleteMode : DeleteMode<MainActivity> = DeleteMode(this)
    //private var deleteMode : ActionMode? = null
    fun getAtivity() = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setOnBackPressedCallback()
        initViews()
        initDb()
        initRecycleView()
        initFabListener()
        initSwitchListsTemplates()
        initEtSearchTextWatcher()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        /*if (BuildCompat.isAtLeastT()) {
            onBackInvokedDispatcher.registerOnBackInvokedCallback(
                OnBackInvokedDispatcher.PRIORITY_DEFAULT
            ) {
                *//**
                 * onBackPressed logic goes here. For instance:
                 * Prevents closing the app to go home screen when in the
                 * middle of entering data to a form
                 * or from accidentally leaving a fragment with a WebView in it
                 *
                 * Unregistering the callback to stop intercepting the back gesture:
                 * When the user transitions to the topmost screen (activity, fragment)
                 * in the BackStack, unregister the callback by using
                 * OnBackInvokeDispatcher.unregisterOnBackInvokedCallback
                 * (https://developer.android.com/reference/kotlin/android/window/OnBackInvokedDispatcher#unregisteronbackinvokedcallback)
                 *//*
            }
        }*/
    }
    fun initViews() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        fabAdd = findViewById(R.id.fabAddList)
        rvToDoLists = findViewById(R.id.rvToDoLists)
        sListsTemplates = findViewById(R.id.sListsTemplates)
        tvHeader = findViewById(R.id.tvTemplatesHeader)
        etSearch = findViewById(R.id.etSearch)
        dummyFocus = findViewById(R.id.layoutMain)
    }
    fun isTemplate() : Boolean {
        return template
    }
    fun setTepmlate(isClicked : Boolean) {
        template = isClicked
    }
    fun isSearchMode() = searchMode
    fun setSearchMode(newValue : Boolean) {
        searchMode = newValue
    }

    override fun getRV(): RecyclerView {
        return rvToDoLists
    }
    fun initFabListener() {
        fabAdd.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val intent = Intent(applicationContext, TasksActivity()::class.java)
                intent.putExtra("toDoListID", -1)
                intent.putExtra("isTemplate", isTemplate())
                intent.putExtra("calledFromTemplate", isTemplate())
                //intent.putExtra("toDoListCreated", false)
                //val intent = Intent(this, TasksActivity()::class.java)
                startActivity(intent)
            }
        })
    }

    fun initDb() {
        //TODO TEMPLATES
        dbTable = ToDoListsDbHelper(this, null, isTemplate())
    }
    fun initRecycleView() {
        val toDoLists = dbTable.getData()
        rvAdapter = ToDoListsAdapter(toDoLists, this)
        rvToDoLists.adapter = rvAdapter
        rvToDoLists.layoutManager = LinearLayoutManager(applicationContext)
    }
    /*fun getUnusedID() : Int {
        //TODO VYRESIT VSECHNY PRIPADY
        val toDoLists = dbTable.getData()
        val sortedDoLists = toDoLists.sortedBy {it.getID()}
        for (i in 0..sortedDoLists.size - 1) {
            if(sortedDoLists[i].getID() != i) {
                return i
            }
        }
        return sortedDoLists.size
    }*/

    fun initSwitchListsTemplates() {
        sListsTemplates.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                if (sListsTemplates.isChecked) {
                    setTepmlate(true)
                }
                else {
                    setTepmlate(false)
                }
                initDb()
                refreshAdapterData(dbTable.getData())
            }
        })
    }

    override fun onResume() {
        super.onResume()
        initDb()
        initRecycleView()
        refreshAdapterData(dbTable.getData())
        //endSearchMode()
    }

    fun refreshAdapterData(newData : ArrayList<ToDoListModel>) {
        //val toDoLists = dbTable.getData()
        rvAdapter.refreshData(newData)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.to_do_lists_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val focus = currentFocus
        hideKeyboard(this)
        if (item.itemId == R.id.menuSearch) {
            when (isSearchMode())
            {
                true -> endSearchMode()
                false -> startSearchMode()
            }
        }
        else endSearchMode()
        handleSortChoosen(item)
        return super.onOptionsItemSelected(item)
    }

    fun handleSortChoosen(item : MenuItem) {
        when(item.itemId) {
            R.id.menuSortDefault -> rvAdapter.sort()
            R.id.menuSortDifficulty -> rvAdapter.sort(1)
            R.id.menuSortImportance -> rvAdapter.sort(2)
        }
    }

    override fun startDeleteMode() {
        val focus = currentFocus
        hideKeyboard(this)
        toolbar.startActionMode(deleteMode)
    }

    override fun endDeleteMode(delete : Boolean) {
        if (delete) {
            val selectedToDoLists = rvAdapter.getSelectedModels()
            val arrayListOfIDs = selectedToDoLists.map {it.getID().toString()}
            val arrayOfIDs = arrayListOfIDs.toTypedArray()
            dbTable.delete(arrayOfIDs)
            val dbTasksTable = TasksDbHelper(this, null, isTemplate(), -1)
            dbTasksTable.delete(arrayOfIDs)
            refreshAdapterData(dbTable.getData())
            endSearchMode()
        }
        deleteMode.endMode()
        rvAdapter.setDeleteMode(false)
        rvAdapter.notifyDataSetChanged()
    }
    fun startSearchMode() {
        fabAdd.visibility = View.GONE
        setSearchMode(true)
        val focus = currentFocus
        //hideKeyboard(focus)
        sListsTemplates.visibility = View.GONE
        tvHeader.visibility = View.GONE
        etSearch.visibility = View.VISIBLE
        val icSearch = toolbar.menu.findItem(R.id.menuSearch)
        icSearch.setIcon(R.drawable.ic_vector_cancel)
        //etSearch.setSelection(0)
        //etSearch.requestFocus()
        showKeyboard(etSearch, this)
        /*val lManager: InputMethodManager =
            getActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        lManager.showSoftInput(etSearch, 0)*/
        searchTextWatcher.updateData(dbTable.getData())
    }
    fun endSearchMode() {
        setSearchMode(false)
        sListsTemplates.visibility = View.VISIBLE
        tvHeader.visibility = View.VISIBLE
        etSearch.visibility = View.GONE
        etSearch.setText("")
        val icSearch = toolbar.menu.findItem(R.id.menuSearch)
        icSearch.setIcon(R.drawable.ic_vector_search)
        refreshAdapterData(dbTable.getData())
        hideKeyboard(this)
        fabAdd.visibility = View.VISIBLE
    }
    fun search(subString: String) {
        val allData = dbTable.getData()
        val searchedData = ArrayList(allData.filter { it.getTitle().contains(subString) })
        refreshAdapterData(searchedData)
    }
    fun initEtSearchTextWatcher() {
        searchTextWatcher = SearchTextWatcher(this)
        etSearch.addTextChangedListener(searchTextWatcher)
        etSearch.addListener(onBackPressedCallback)
    }
    override fun onStop() {
        endSearchMode()
        endDeleteMode(false)
        super.onStop()
    }
    fun setOnBackPressedCallback() {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val focus = currentFocus
                if (((focus == dummyFocus) or (focus == null)) and (deleteMode.getDeleteMode() == null) and (!isSearchMode())) finish()
                if ((focus == dummyFocus) or (focus == null)) endSearchMode()
                endDeleteMode(false)
                hideKeyboard<MainActivity>(getAtivity())
            }
        }
    }

    override fun callSelectAllOnAdapter() {
        rvAdapter.selectAll()
    }

}

interface Connector {
    fun startDeleteMode()
    fun endDeleteMode(delete : Boolean)
    fun callSelectAllOnAdapter()
}

interface Callback {
    fun makeCallback()
}
