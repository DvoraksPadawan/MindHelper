package pac.underpackage.brainhelper.Models

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

//TODO tady musi byt parametry konstruktoru nepovinne
class TaskModel(ID : Int,
                title : String = "",
                deadline : LocalDateTime = LocalDateTime.MAX,
                difficulty : Int = 0,
                importance : Int = 0,
                completed : Int = 0) :
    Model(ID, title, deadline, difficulty, importance, completed) {
    /*fun isNew() = new
    fun setNew(newValue : Boolean) {
        new = newValue
    }*/
    fun changeDate(year : Int, month : Int, day: Int) {
        var newDeadline = getDeadline()
        if ((newDeadline == LocalDateTime.MAX)
            or (newDeadline == LocalDateTime.MIN))
            newDeadline = LocalDate.now().atTime(LocalTime.MAX)
        newDeadline = newDeadline.withYear(year)
        newDeadline = newDeadline.withMonth(month)
        newDeadline = newDeadline.withDayOfMonth(day)
        setDeadline(newDeadline)
    }
    fun changeTime(hour : Int, minute : Int) {
        var newDeadline = getDeadline()
        newDeadline = newDeadline.withHour(hour)
        newDeadline = newDeadline.withMinute(minute)
        setDeadline(newDeadline)
    }
    fun copy() : TaskModel {
        val oldModel = this
        val newModel = TaskModel(oldModel.getID(), oldModel.getTitle(), oldModel.getDeadline(), oldModel.getDifficulty(), oldModel.getImportance(), oldModel.getCompleted())
        return newModel
    }
}