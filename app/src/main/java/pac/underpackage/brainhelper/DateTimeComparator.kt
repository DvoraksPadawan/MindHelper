package pac.underpackage.brainhelper

import pac.underpackage.brainhelper.Models.Model
import java.time.LocalDateTime

class DateTimeComparator<T : Model> : Comparator<T> {
    override fun compare(model1: T, model2: T): Int {
        val dateTime1 = model1.getDeadline()
        val dateTime2 = model2.getDeadline()
        if (dateTime1 == dateTime2) return 0
        else if ((dateTime1 == LocalDateTime.MIN) and (dateTime2 <= LocalDateTime.now())) return 1
        else if ((dateTime2 == LocalDateTime.MIN) and (dateTime1 <= LocalDateTime.now())) return -1
        else return dateTime1.compareTo(dateTime2)
    }
}