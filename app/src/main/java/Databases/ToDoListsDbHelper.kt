package Databases

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import pac.underpackage.brainhelper.Models.ToDoListModel
import java.time.LocalDateTime

class ToDoListsDbHelper(context: Context, factory:SQLiteDatabase.CursorFactory?, template: Boolean) : DatabaseHelper(context, factory, template) {
    fun addToDoList(ID: Int, title: String) {
        val db = this.writableDatabase
        var row = ContentValues()
        row.put(ID_COL, ID)
        row.put(TITLE_COL, title)
        //TODO TOTO DOCASNE:
        row.put(DEADLINE_COL, LocalDateTime.MAX.toString())
        row.put(DIFFICULTY_COL, -1)
        row.put(IMPORTANCE_COL, -1)
        //val int = if (completed) 1 else 0
        row.put(COMPLETED_COL, 0)
        //TODO
        db.insert(this.getTableName(), null, row)
        db.close()
        Log.d("my_tag", "added:" + title + getTableName())
    }
    fun getData() : ArrayList<ToDoListModel> {
        val db = this.readableDatabase
        val query = "SELECT * FROM " + getTableName()
        var toDoLists = arrayListOf<ToDoListModel>()
        var toDoListModel : ToDoListModel
        var cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        if (cursor.getCount() > 0) {
            do {
                val ID = cursor.getInt(cursor.getColumnIndexOrThrow(ID_COL))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE_COL))
                val deadline = cursor.getString(cursor.getColumnIndexOrThrow(DEADLINE_COL))
                val difficulty = cursor.getInt(cursor.getColumnIndexOrThrow(DIFFICULTY_COL))
                val importance = cursor.getInt(cursor.getColumnIndexOrThrow(IMPORTANCE_COL))
                val completed = cursor.getInt(cursor.getColumnIndexOrThrow(COMPLETED_COL))
                Log.d("my_tag2", title)
                toDoListModel = ToDoListModel(ID = ID,title = title, LocalDateTime.parse(deadline), difficulty, importance, completed)
                toDoLists.add(toDoListModel)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return toDoLists
    }
    fun getToDoListsNames() : List<String> {
        val db = this.readableDatabase
        val query = "SELECT " + TITLE_COL + " FROM " + getTableName()
        var toDoListsTitles = ArrayList<String>()
        var cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        if (cursor.getCount() > 0) {
            do {
                val title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE_COL))
                toDoListsTitles.add(title)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return toDoListsTitles.toList()
    }
    fun updateRowTitle(ID : Int, title: String) {
        val db = this.writableDatabase
        var row = ContentValues()
        row.put(TITLE_COL, title)
        db.update(getTableName(), row, ID_COL + "=?", arrayOf(ID.toString()))
        db.close()
    }
    fun updateRowNumbers(ID : Int, deadline : String, difficulty : Int, importance : Int, completed: Int) {
        val db = this.writableDatabase
        var row = ContentValues()
        row.put(DEADLINE_COL, deadline)
        row.put(DIFFICULTY_COL, difficulty)
        row.put(IMPORTANCE_COL, importance)
        row.put(COMPLETED_COL, completed)
        db.update(getTableName(), row, ID_COL + "=?", arrayOf(ID.toString()))
        db.close()
    }
    fun getHeader(ID : Int) : String {
        val db = this.readableDatabase
        val query = "SELECT " + TITLE_COL + " FROM " + getTableName() + " WHERE " + ID_COL + "=?"
        val cursor = db.rawQuery(query, arrayOf(ID.toString()))
        cursor.moveToFirst()
        val header = cursor.getString(cursor.getColumnIndexOrThrow(TITLE_COL))
        cursor.close()
        db.close()
        return header
    }
    fun delete(arrayOfIDs : Array<String>) {
        val db = this.writableDatabase
        db.beginTransaction()
        for (ID in arrayOfIDs)
            db.delete(getTableName(), ID_COL +"=?", arrayOf(ID))
        db.setTransactionSuccessful()
        db.endTransaction()
        db.close()
    }

}