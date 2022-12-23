package Databases

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


//TODO NEOPAKOVAT METODY NA DB
abstract class DatabaseHelper(context: Context, factory: SQLiteDatabase.CursorFactory?, val template : Boolean) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    private lateinit var tableName : String
    init {
        setTableName()
    }
    override fun onCreate(db: SQLiteDatabase) {
        //TODO TEMPLATES
        var query = ("CREATE TABLE " + TO_DO_LISTS_TABLE_NAME + " (" +
                ID_COL + " INTEGER PRIMARY KEY, " +
                TITLE_COL + " TEXT, " +
                DEADLINE_COL + " TEXT, " +
                DIFFICULTY_COL + " INTEGER, " +
                IMPORTANCE_COL + " INTEGER, " +
                COMPLETED_COL + " INTEGER" +
                ")")
        db.execSQL(query)
        query = ("CREATE TABLE " + TEMPLATES_LISTS_TABLE_NAME + " (" +
                ID_COL + " INTEGER PRIMARY KEY, " +
                TITLE_COL + " TEXT, " +
                DEADLINE_COL + " TEXT, " +
                DIFFICULTY_COL + " INTEGER, " +
                IMPORTANCE_COL + " INTEGER, " +
                COMPLETED_COL + " INTEGER" +
                ")")
        db.execSQL(query)
        query = ("CREATE TABLE " + TASKS_TABLE_NAME + " (" +
                ID_COL + " INTEGER, " +
                TASK_ID_COL + " INTEGER, " +
                TITLE_COL + " TEXT, " +
                DEADLINE_COL + " TEXT, " +
                DIFFICULTY_COL + " INTEGER, " +
                IMPORTANCE_COL + " INTEGER, " +
                COMPLETED_COL + " INTEGER" +
                //"FOREIGN KEY (" + TO_DO_LIST_ID_COL + ") REFERENCES " + TO_DO_LISTS_TABLE_NAME + "(" + ID_COL + "), " +
                //"PRIMARY KEY (" + TO_DO_LIST_ID_COL + ", " + TASK_ID_COL + ")" +
                ")")
        db.execSQL(query)
        query = ("CREATE TABLE " + TEMPLATES_TASKS_TABLE_NAME + " (" +
                ID_COL + " INTEGER, " +
                TASK_ID_COL + " INTEGER, " +
                TITLE_COL + " TEXT, " +
                DEADLINE_COL + " TEXT, " +
                DIFFICULTY_COL + " INTEGER, " +
                IMPORTANCE_COL + " INTEGER, " +
                COMPLETED_COL + " INTEGER" +
                //"FOREIGN KEY (" + TO_DO_LIST_ID_COL + ") REFERENCES " + TEMPLATES_LISTS_TABLE_NAME + "(" + ID_COL + ")" +
                //"PRIMARY KEY (" + TO_DO_LIST_ID_COL + ", " + TASK_ID_COL + ")" +
                ")")
        db.execSQL(query)
    }
    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        // this method is to check if table already exists
        db.execSQL("DROP TABLE IF EXISTS " + TO_DO_LISTS_TABLE_NAME)
        onCreate(db)
        db.execSQL("DROP TABLE IF EXISTS " + TASKS_TABLE_NAME)
        onCreate(db)
    }
    fun getTableName() : String {
        return tableName
    }
    fun setTableName() {
        if (this is TasksDbHelper) {
            if (isTemplate()) tableName = TEMPLATES_TASKS_TABLE_NAME
            else tableName = TASKS_TABLE_NAME
        }
        else {
            if (isTemplate()) tableName = TEMPLATES_LISTS_TABLE_NAME
            else tableName = TO_DO_LISTS_TABLE_NAME
        }
    }
    fun isTemplate() : Boolean {
        return template
    }
    companion object {
        private val DATABASE_NAME = "DATABASE SAMPLE29"
        private val DATABASE_VERSION = 1
        private val TASKS_TABLE_NAME = "tasks"
        private val TO_DO_LISTS_TABLE_NAME = "toDoLists"
        private val TEMPLATES_LISTS_TABLE_NAME = "templatesLists"
        private val TEMPLATES_TASKS_TABLE_NAME = "tamplatesTasks"
        fun getTASKS_TABLE_NAME() : String {
            return TASKS_TABLE_NAME
        }
        fun getTO_DO_LISTS_TABLE_NAME() : String {
            return TO_DO_LISTS_TABLE_NAME
        }
        fun getTEMPLATES_LISTS_TABLE_NAME() : String {
            return TEMPLATES_LISTS_TABLE_NAME
        }
        fun getTEMPLATES_TASKS_TABLE_NAME() : String {
            return TEMPLATES_TASKS_TABLE_NAME
        }
        val ID_COL = "id"
        val TITLE_COL = "title"
        val IMPORTANCE_COL = "importance"
        val DIFFICULTY_COL = "difficulty"
        val DEADLINE_COL = "deadline"
        val COMPLETED_COL = "completed"

        val TASK_ID_COL = "taskId"
    }

}