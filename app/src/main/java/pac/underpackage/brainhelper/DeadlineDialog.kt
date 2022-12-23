package pac.underpackage.brainhelper

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import pac.underpackage.brainhelper.Models.TaskModel
import java.time.LocalDateTime
import java.util.*

class DeadlineDialog(private val task : TaskModel, private val context: Context) {
    //lateinit var dateDialog : DatePickerDialog
    //lateinit var timeDialog : TimePickerDialog
    inner class DateListener : DatePickerDialog.OnDateSetListener {
        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
            task.changeDate(year, month + 1, dayOfMonth)
            if (view == null)
                Log.d("my_tag3", "wasnt choosen")
            else Log.d("my_tag3", "choosen")
        }
    }
    inner class TimeListener() : TimePickerDialog.OnTimeSetListener {
        override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
            task.changeTime(hourOfDay, minute)
        }
    }
    fun startDateDialog() {
        var dateTime = task.getDeadline()
        if ((dateTime == LocalDateTime.MAX) or (dateTime == LocalDateTime.MIN)) {
            dateTime = LocalDateTime.now()
        }
        val year = dateTime.year
        val month = dateTime.monthValue
        val day = dateTime.dayOfMonth
        val listener = DateListener()
        val dialog = DatePickerDialog(context, listener, year, month - 1, day)
        dialog.datePicker.firstDayOfWeek = Calendar.MONDAY
        dialog.show()
    }
    fun startTimeDialog() {
        var dateTime = task.getDeadline()
        /*if (dateTime == LocalDateTime.MAX || dateTime == LocalDateTime.MIN) {
            dateTime = LocalDateTime.now()
        }*/
        val hour = dateTime.hour
        val minute = dateTime.minute
        val listener = TimeListener()
        val dialog = TimePickerDialog(context, listener, hour, minute, true)
        dialog.show()
    }
}