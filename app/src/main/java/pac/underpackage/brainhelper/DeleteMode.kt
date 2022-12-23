package pac.underpackage.brainhelper

import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
//import com.example.brainhelper.R

//import com.example.brainhelper.R

class DeleteMode<T : AppCompatActivity>(private val activity : T) : ActionMode.Callback {
    private var deleteMode : ActionMode? = null
    private var delete = false
    fun getDeleteMode() = deleteMode
    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        deleteMode = mode
        this.delete = false
        if (activity is MainActivity)
            mode?.menuInflater?.inflate(R.menu.delete_mode_menu, menu)
        else
            mode?.menuInflater?.inflate(R.menu.delete_mode_menu_tasks, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return false
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.dMenuDelete -> endMode(true)
            R.id.dMenuDeleteAll -> (activity as Connector).callSelectAllOnAdapter()
        }
        if (activity is TasksActivity)
            if (item?.itemId == R.id.dMenuCopy) {
                activity.copy()
                endMode()
            }
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        deleteMode = null
        (activity as Connector).endDeleteMode(delete)
    }
    fun endMode(delete : Boolean = false) {
        this.delete = delete
        if (deleteMode == null) return
        deleteMode?.finish()
    }
}