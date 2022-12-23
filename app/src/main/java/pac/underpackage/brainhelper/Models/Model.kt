package pac.underpackage.brainhelper.Models

import pac.underpackage.brainhelper.Callback
import java.time.LocalDateTime

abstract class Model(
    private var ID : Int,
    private var title : String,
    private var deadline : LocalDateTime,
    private var difficulty : Int,
    private var importance : Int,
    private var completed : Int
    ) {
    private var callback : Callback? = null
    //private lateinit var deadline : LocalDateTime
    init {
        //setDeadlineFromString(deadlineString)
    }
    fun setCallback(newValue : Callback) {
        callback = newValue
    }
    fun getCallback() = callback
    fun getID() : Int {
        return ID
    }
    fun setID(new : Int) {
        ID = new
    }
    fun getTitle() : String {
        return title
    }
    fun setTitle(new : String) {
        title = new
    }
    /*fun getDeadlineString() : String {
        return deadlineString
    }
    fun setDeadlineString(newValue : String) {
        deadlineString = newValue
    }*/

    fun getDeadline() = deadline
    fun setDeadline(newValue : LocalDateTime) {
        deadline = newValue
        getCallback()?.makeCallback()
    }
    fun getDifficulty() : Int {
        return difficulty
    }
    fun setDifficulty(new : Int)
    {
        difficulty = new
    }
    fun getImportance() : Int {
        return importance
    }
    fun setImportance(new : Int) {
        importance = new
    }
    fun isCompleted() : Boolean {
        if (completed == 1) return true
        else return false
    }
    fun getCompleted() = completed
    fun setCompleted(newValue: Boolean) {
        if (newValue) completed = 1
        else completed = 0
    }
    private var selected = false
    fun isSelected() = selected
    fun setSelected(newValue : Boolean) {
        selected = newValue
    }
    companion object {
        /*private val formatter = DateTimeFormatter.ofPattern("dd.MM.uuuu HH:mm")
        fun deadlineFromString(deadlineString : String) : LocalDateTime {
            val deadline : LocalDateTime
            if (deadlineString == LocalDateTime.MIN.toString()) {
                deadline = LocalDateTime.MIN
            }
            else if (deadlineString == LocalDateTime.MAX.toString()) {
                deadline = LocalDateTime.MAX
            }
            else {
                deadline = LocalDateTime.parse(deadlineString)
            }
            return deadline
        }*/
        /*fun deadlineToString(deadline : LocalDateTime) : String {
            val deadlineString = deadline.format(formatter)
            return deadlineString
        }*/
    }
}