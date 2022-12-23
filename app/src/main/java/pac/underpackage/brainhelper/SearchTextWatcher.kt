package pac.underpackage.brainhelper

import android.text.Editable
import android.text.TextWatcher
import pac.underpackage.brainhelper.Models.ToDoListModel

class SearchTextWatcher(private val activity: MainActivity) : TextWatcher {
    private lateinit var allData : ArrayList<ToDoListModel>
    fun updateData(newData : ArrayList<ToDoListModel>) {
        allData = newData
    }
    fun returnInput(input : CharSequence?) {
        activity.search(input.toString())
    }
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        returnInput(s)
    }

    override fun afterTextChanged(s: Editable?) {
    }
}