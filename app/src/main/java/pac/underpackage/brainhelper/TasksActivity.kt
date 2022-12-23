package pac.underpackage.brainhelper

import Databases.TasksDbHelper
import Databases.ToDoListsDbHelper
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pac.underpackage.brainhelper.Adapters.TasksAdapter
import pac.underpackage.brainhelper.Models.TaskModel
//import com.example.brainhelper.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDateTime
import kotlin.math.roundToInt
fun androidx.appcompat.widget.AppCompatEditText?.onKeyPreIme(keyCode : Int, event: KeyEvent) : Boolean {
    Log.d("my_tag5", "preIme")
    return false
}
fun EditText?.setText(s : CharSequence) {
    this.setText(s)
    Log.d("my_tag5", "preIme")
}
//TODO TEMPLATE DO PARAMETRU
class TasksActivity() : AActivity(), Connector {
    //private val indexOfToDoList : Int, private val isTemplate : Boolean
    fun getActivity() = this
    private var indexOfToDoListFrom = -1
    private var indexOfToDoListTo = -1
    private lateinit var etHeader : CustomEditText
    private var toDoListCreated : Boolean = false
    //private lateinit var toDoListsNames : List<String>
    //TODO: tableName muze byt template
    private lateinit var dbTableFrom : TasksDbHelper
    private lateinit var dbTableTo : TasksDbHelper
    //TODO TEMPLATE
    private lateinit var dbSuperTableTo : ToDoListsDbHelper
    private var template : Boolean = false
    private lateinit var rvTasks : RecyclerView
    private lateinit var rvAdapter : TasksAdapter
    private lateinit var fabAddTask : FloatingActionButton
    private lateinit var toolbar : androidx.appcompat.widget.Toolbar
    private var fromTemplate : Boolean = false
    private var deleteMode : DeleteMode<TasksActivity> = DeleteMode(this)
    private val saveDialogHandler = SaveDialogHandler(this)
    fun getIndexOfToDoListFrom() : Int {
        return indexOfToDoListFrom
    }
    fun setIndexOfToDoListFrom(index : Int) {
        indexOfToDoListFrom = index
    }
    fun getIndexOfToDoListTo() : Int {
        return indexOfToDoListTo
    }
    fun setIndexOfToDoListTo(index : Int) {
        indexOfToDoListTo = index
    }
    fun isTemplate() : Boolean {
        return template
    }
    fun setTemplate(aTemplate : Boolean) {
        template = aTemplate
    }
    fun getToDoListCreated() : Boolean {
        return toDoListCreated
    }
    fun setToDoListCreated(created : Boolean) {
        toDoListCreated = created
    }
    fun calledFromTemplate() = fromTemplate
    fun setFromTemplate(new : Boolean) {
        fromTemplate = new
    }
    private var header = ""
    fun getHeader() = header
    fun setHeader(newValue : String) {
        header = newValue
    }

    override fun getRV(): RecyclerView {
        return rvTasks
    }
    /*private var canContinue : Boolean = false
    fun canContinue() = canContinue
    fun setCanContinue(newValue : Boolean) {
        canContinue = newValue
    }*/
    fun getEtHeader() = etHeader


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks)
        Log.d("my_tag4", "on Create")

        setOnBackPressedCallback()
        initViews()
        getFromIntent()
        initDbs()
        initEtHeader()
        //initEtHeaderTextWatcher()
        initRecycleview()
        initFabAddTask()
        //Log.d("my_tag3", "color" + rvTasks.getBackgroundTintList().toString())
        Log.d("my_tag3", "color" + rvTasks.solidColor)

        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }
    fun initViews() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        etHeader = findViewById(R.id.etHeader)
        rvTasks = findViewById(R.id.rvTasks)
        fabAddTask = findViewById(R.id.fabAddTask)
        dummyFocus = findViewById(R.id.layoutTasks)
    }

    fun initEtHeader() {
        //hideKeyboard()
        etHeader.addListener(onBackPressedCallback)
        if (!getToDoListCreated()) {
            showKeyboard(etHeader, this)
            return
        }
        setHeader(dbSuperTableTo.getHeader(getIndexOfToDoListFrom()))
        etHeader?.setText(getHeader())
    }



    /*fun initEtHeaderTextWatcher() {
        etHeader.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //setCanContinue(false)
                *//*val toDoListsNames = getToDoListsNames()
                if (!(toDoListsNames.contains(p0.toString()))) {
                    if (!getToDoListCreated()) {
                        dbSuperTableTo.addToDoList(getIndexOfToDoListTo(), p0.toString())
                        setToDoListCreated(true)
                    }
                    else {
                        dbSuperTableTo.updateRowTitle(getIndexOfToDoListTo(), p0.toString())
                    }
                }*//*
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }*/
    fun initRecycleview() {
        val tasks = dbTableFrom.getData(getIndexOfToDoListFrom())
        rvAdapter = TasksAdapter(ArrayList<TaskModel>(), this)
        if (calledFromTemplate() and !isTemplate())
            rvAdapter.addModels(tasks, getIndexOfToDoListTo())
        else
            rvAdapter.addModels(tasks)
        //rvAdapter = TasksAdapter(tasks, this)
        rvTasks.adapter = rvAdapter
        rvTasks.layoutManager = LinearLayoutManager(applicationContext)
        rvAdapter.notifyDataSetChanged()
        Log.d("my_tag2", "initRecycleview")
    }
    fun copy() {
        val selected = rvAdapter.getSelectedModels()
        val newTasks = ArrayList<TaskModel>()
        for (task in selected) {
            val copy = task.copy()
            newTasks.add(copy)
        }
        if (calledFromTemplate() and !isTemplate())
            rvAdapter.addModels(newTasks, getIndexOfToDoListTo())
        else
            rvAdapter.addModels(newTasks)
    }
    fun initFabAddTask() {
        fabAddTask.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                //rvAdapter.addTask(TaskModel(ID = getIndexOfToDoListTo()))
                rvAdapter.addModel(TaskModel(ID = getIndexOfToDoListTo()), newModel = true)
            }
        })
    }
    fun getFromIntent() {
        setIndexOfToDoListFrom(intent.getIntExtra("toDoListID", -1))
        setTemplate(intent.getBooleanExtra("isTemplate", false))
        setToDoListCreated(intent.getBooleanExtra("toDoListCreated", false))
        setFromTemplate(intent.getBooleanExtra("calledFromTemplate", false))
    }
    //TODO templates
    fun initDbs() {
        dbSuperTableTo = ToDoListsDbHelper(this, null, isTemplate())
        if (!getToDoListCreated()) setIndexOfToDoListTo(getUnusedID())
        else setIndexOfToDoListTo(getIndexOfToDoListFrom())
        /*if (calledFromTemplate() and !isTemplate())
            dbTableFrom = TasksDbHelper(this, null, calledFromTemplate(), getIndexOfToDoListFrom(), getIndexOfToDoListTo())
        else*/
        dbTableFrom = TasksDbHelper(this, null, calledFromTemplate(), getIndexOfToDoListFrom())
        dbTableTo = TasksDbHelper(this, null, isTemplate(), getIndexOfToDoListTo())
    }
    fun getToDoListsNames() : List<String> {
        var db = ToDoListsDbHelper(this, null, false)
        val toDoListsTitles = db.getToDoListsNames()
        db = ToDoListsDbHelper(this, null, true)
        val templateListsTitles = db.getToDoListsNames()
        val names = toDoListsTitles + templateListsTitles
        return names
    }
    //TODO SMAZAT:
    fun refreshAdapterData() {
        val tasks = dbTableFrom.getData(indexOfToDoListFrom)
        rvAdapter.refreshData(tasks)
    }
    //if we would put it into onPasue, then the user would see, how its changed after sorting:
    //we have to call onPause, because we wouldnt see todolists updated
    override fun onPause() {
        //rvAdapter.notifyDataSetChanged()
        super.onPause()
        hideKeyboard(this)
        endDeleteMode(false)
        save()
    }
    fun save() {
        saveToDoListName(false)
        if (!getToDoListCreated()) return
        rvAdapter.sort()
        val tasks = rvAdapter.getData()
        dbTableTo.addTasks(tasks)
        calculateRowAtributes(getIndexOfToDoListTo(), tasks)
    }
    fun calculateRowAtributes(ID: Int, tasks : ArrayList<TaskModel>) {
        //TODO WARNING HARDCODED CONSTANTS
        var minDifficulty = 5
        var maxImportance = 0
        var completed = 0
        var minDeadline = LocalDateTime.MAX.toString()
        for (task in tasks) {
            if (task.getDifficulty() < minDifficulty) minDifficulty = task.getDifficulty()
            if (task.getImportance() > maxImportance) maxImportance = task.getImportance()
            if (task.isCompleted()) completed += 1
        }
        if (tasks.size > 0) {
            minDeadline = tasks[0].getDeadline().toString()
            val toPercents = 100.0/tasks.size
            completed = (completed * toPercents).roundToInt()
        }
        else completed = 100
        dbSuperTableTo.updateRowNumbers(ID, minDeadline, minDifficulty, maxImportance, completed)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if ((isTemplate()) and (calledFromTemplate())) menuInflater.inflate(R.menu.template_task_menu, menu)
        else menuInflater.inflate(R.menu.tasks_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val focus = currentFocus
        hideKeyboard(this)
        if (item?.itemId == R.id.menuFromTemplate) {
            //TODO PROBLEM SOURCE
            createFromTemplate()
        }
        handleSortChoosen(item)
        return super.onOptionsItemSelected(item)
    }
    fun createFromTemplate() {
        if (!saveToDoListName()) return
        val intent = Intent(applicationContext, TasksActivity()::class.java)
        intent.putExtra("toDoListID", getIndexOfToDoListTo())
        intent.putExtra("isTemplate", false)
        //TODO CDROJ PROBLEMU
        //intent.putExtra("toDoListCreated", true)
        intent.putExtra("calledFromTemplate", true)
        //save()
        //Thread.sleep(10000)
        startActivity(intent)
        finish()
    }


    override fun startDeleteMode() {
        fabAddTask.visibility = View.GONE
        val focus = currentFocus
        hideKeyboard(this)
        toolbar.startActionMode(deleteMode)
    }

    override fun endDeleteMode(delete : Boolean) {
        if (delete) {
            val all = rvAdapter.getData()
            val selected = rvAdapter.getSelectedModels()
            for (task in selected) {
                all.remove(task)
            }
        }
        deleteMode.endMode()
        rvAdapter.setDeleteMode(false)
        rvAdapter.notifyDataSetChanged()
        fabAddTask.visibility = View.VISIBLE
    }
    //TODO TOTO SE OPAKUJE V PROGRAMU
    fun getUnusedID() : Int {
        //TODO VYRESIT VSECHNY PRIPADY
        val toDoLists = dbSuperTableTo.getData()
        val sortedDoLists = toDoLists.sortedBy {it.getID()}
        for (i in 0..sortedDoLists.size - 1) {
            if(sortedDoLists[i].getID() != i) {
                return i
            }
        }
        return sortedDoLists.size
    }
    fun initCallbacks(tasks : ArrayList<TaskModel>) {
        for (task in tasks) {
            task.setCallback(rvAdapter)
        }
    }
    fun setOnBackPressedCallback() {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val focus = currentFocus
                if (((focus == dummyFocus) or (focus == null)) and (deleteMode.getDeleteMode() == null)) {
                    //if (canContinue()) finish()
                    if (saveToDoListName())
                        finish()
                    //if (canContinue()) finish()
                }
                endDeleteMode(false)
                hideKeyboard(getActivity())

            }
        }
    }

    fun saveToDoListName(showDialog : Boolean = true) : Boolean {
        val newHeader = etHeader?.text.toString()
        val toDoListsNames = getToDoListsNames()
        when {
            newHeader == "" ->
                if (showDialog)
                    saveDialogHandler.build(!getToDoListCreated())
            newHeader == getHeader() ->
                //setCanContinue(true)
                return true
            toDoListsNames.contains(newHeader) ->
                if (showDialog)
                    saveDialogHandler.build(!getToDoListCreated())
            else -> {
                if (!getToDoListCreated()) {
                    dbSuperTableTo.addToDoList(getIndexOfToDoListTo(), newHeader)
                    setToDoListCreated(true)
                }
                else
                    dbSuperTableTo.updateRowTitle(getIndexOfToDoListTo(), newHeader)
                //setCanContinue(true)
                return true
            }
        }
        return false
        /*when (saveDialogHandler.handleIfCanContinue(newHeader)) {
            true -> {
                setCanContinue(true)
                if (newHeader == getHeader()) return
                if (!getToDoListCreated()) {
                    dbSuperTableTo.addToDoList(getIndexOfToDoListTo(), newHeader)
                    setToDoListCreated(true)
                }
                else {
                    dbSuperTableTo.updateRowTitle(getIndexOfToDoListTo(), newHeader)
                }
            }
            false -> saveDialogHandler.build(!getToDoListCreated())
        }*/
    }

    fun handleSortChoosen(item : MenuItem) {
        when(item.itemId) {
            R.id.menuSortDefault -> rvAdapter.sort()
            R.id.menuSortDifficulty -> rvAdapter.sort(1)
            R.id.menuSortImportance -> rvAdapter.sort(2)
        }
    }
    override fun callSelectAllOnAdapter() {
        rvAdapter.selectAll()
    }
}