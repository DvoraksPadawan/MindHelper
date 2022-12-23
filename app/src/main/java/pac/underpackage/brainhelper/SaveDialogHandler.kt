package pac.underpackage.brainhelper

import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

class SaveDialogHandler(private val activity: TasksActivity) {
    fun handleIfCanContinue(newHeader : String) : Boolean {
        //todolist name wasnt changed
        if (newHeader == activity.getHeader()) return true
        val toDoListsNames = activity.getToDoListsNames()
        //todolist name is already taken
        /*if (toDoListsNames.contains(newHeader)) {
            return false
            when (activity.getToDoListCreated()) {
                //todolist was already saved to db, but with different name
                true -> return 0
                //todolist wasnt created yet
                false -> return -1
            }
        }
        //new todolist name is free
        else return true*/
        return !toDoListsNames.contains(newHeader)
    }
    fun build(newTodolist : Boolean) {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("This list name already exist")

        //set content area
        when (newTodolist) {
            true -> builder.setMessage("If you will continue, this list won't be saved")
            else -> builder.setMessage("If you will continue, this list will be saved with its previous name")
        }

        //set negative button
        /*builder.setPositiveButton(
            "Update Now") { dialog, id ->
            // User clicked Update Now button
            Toast.makeText(activity, "Updating your device", Toast.LENGTH_SHORT).show()
        }*/
        builder.setPositiveButton("Continue", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                //activity.setCanContinue(true)
                activity.finish()
            }
        })
        /*builder.setNegativeButton(
            "Cancel") { dialog, id ->
            // User cancelled the dialog
        }*/
        builder.setNegativeButton( "Change name", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                activity.showKeyboard(activity.getEtHeader(), activity)
            }
        })
        builder.show()
    }
}