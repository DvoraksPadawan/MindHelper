package Databases

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import pac.underpackage.brainhelper.Models.TaskModel
import java.time.LocalDateTime

class TasksDbHelper(context: Context,
                    factory: SQLiteDatabase.CursorFactory?,
                    template: Boolean, private val toDoListIndex: Int) : DatabaseHelper(context, factory, template) {
    lateinit var superTableName : String
    init {
        setSuperTableName()
    }
    fun getToDoListIndex() : Int {
        return toDoListIndex
    }
    fun setSuperTableName() {
        if (isTemplate()) superTableName = getTEMPLATES_LISTS_TABLE_NAME()
        else superTableName = getTO_DO_LISTS_TABLE_NAME()
    }
    //fun getNewID() = newID
    fun addTasks(tasks : ArrayList<TaskModel>) {
        val db = this.writableDatabase
        //var query = "DELETE FROM " + getTableName() + " WHERE " + TO_DO_LIST_ID_COL + "=?"
        //db.rawQuery(query, arrayOf(getToDoListIndex().toString()))
        db.delete(getTableName(), ID_COL + "=?", arrayOf(getToDoListIndex().toString()))
        db.beginTransaction()
        var i : Int = 0
        for (model in tasks) {
            var row = ContentValues()
            //TODO TY RADKY POD ID TU JSOU DOCASNE
            row.put(ID_COL, model.getID())
            row.put(TITLE_COL, model.getTitle())
            row.put(TASK_ID_COL, i)
            row.put(DEADLINE_COL, model.getDeadline().toString())
            Log.d("my_tag9", model.getDeadline().toString())
            row.put(DIFFICULTY_COL, model.getDifficulty())
            row.put(IMPORTANCE_COL, model.getImportance())
            row.put(COMPLETED_COL, model.getCompleted())
            //TODO TY RADKY POD ID TU JSOU DOCASNE
            i += 1
            db.insert(getTableName(), null, row)
        }
        db.setTransactionSuccessful()
        db.endTransaction()
        db.close()
    }
    //TODO DATA JEN ON JEDNOHO TODOLISTU
    fun getData(toDoListID: Int) : ArrayList<TaskModel> {
        val db = this.readableDatabase
        val query = "SELECT * FROM " + getTableName() + " WHERE " + ID_COL + "=?"
        var tasks = arrayListOf<TaskModel>()
        var task : TaskModel
        var cursor = db.rawQuery(query, arrayOf(toDoListID.toString()))
        cursor.moveToFirst()
        if (cursor.getCount() > 0) {
            do {
                //TODO TADY TY ID SPRAVIT
                var ID = cursor.getInt(cursor.getColumnIndexOrThrow(ID_COL))
                //if (getNewID() > -1) ID = newID
                val title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE_COL))
                val deadline = cursor.getString(cursor.getColumnIndexOrThrow(DEADLINE_COL))
                val difficulty = cursor.getInt(cursor.getColumnIndexOrThrow(DIFFICULTY_COL))
                val importance = cursor.getInt(cursor.getColumnIndexOrThrow(IMPORTANCE_COL))
                val completed = cursor.getInt(cursor.getColumnIndexOrThrow(COMPLETED_COL))
                //Log.d("my_tag9", Model.deadlineFromString(deadline).toString())
                task = TaskModel(ID, title, LocalDateTime.parse(deadline), difficulty, importance, completed)
                Log.d("my_tag9", task.getDeadline().toString())
                tasks.add(task)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return tasks
    }
    fun delete(arrayOfIDs : Array<String>) {
        val db = this.writableDatabase
        db.beginTransaction()
        for (ID in arrayOfIDs)
            db.delete(getTableName(), ID_COL + "=?", arrayOf(ID))
        db.setTransactionSuccessful()
        db.endTransaction()
        db.close()
    }

    companion object {
        private val TEMP_TABLE_NAME = "tempTable"
    }
}