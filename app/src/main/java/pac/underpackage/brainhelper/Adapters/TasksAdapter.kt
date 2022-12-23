package pac.underpackage.brainhelper.Adapters

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.core.content.ContextCompat
import pac.underpackage.brainhelper.DeadlineDialog
import pac.underpackage.brainhelper.Models.TaskModel
import pac.underpackage.brainhelper.R
//import com.example.brainhelper.R
import pac.underpackage.brainhelper.TasksActivity
import java.time.LocalDateTime
import java.time.LocalTime

class TasksAdapter(models: ArrayList<TaskModel>, activity: TasksActivity) :
        AAdapter<TaskModel, TasksAdapter.ViewHolder, TasksActivity>(models, activity) {

    inner class ViewHolder(view: View) : AViewHolder(view, getActivity()) {
        val titleTextWatcher = TitleTextWatcher()
        val sDifficultyListener = SDifficultyListener()
        val sImportanceListener = SImportanceListener()
        val ibCheckOnClickListener = IbCheckOnClickListener()
        val sDateOnSelectedListener = SDateOnSelectedListener()
        val sTimeOnSelectedListener = STimeOnSelectedListener()
        val tvSImportanceListener = TextViewSpinnerListener(sImportance, sImportanceListener)
        val tvSDifficultyListener = TextViewSpinnerListener(sDifficulty, sDifficultyListener)
        init {
        }
        override fun setListeners() {
            if (!bDeleteCheck.hasOnClickListeners()) return
            super.setListeners()
            itemView.setOnClickListener(ibCheckOnClickListener)
            etRowHeader.addTextChangedListener(titleTextWatcher)
            sDifficulty.onItemSelectedListener = sDifficultyListener
            sDifficulty.setOnTouchListener(sDifficultyListener)
            sImportance.onItemSelectedListener = sImportanceListener
            sImportance.setOnTouchListener(sImportanceListener)
            ibCheck.setOnClickListener(ibCheckOnClickListener)
            sDate.onItemSelectedListener = sDateOnSelectedListener
            sDate.setOnTouchListener(sDateOnSelectedListener)
            sTime.onItemSelectedListener = sTimeOnSelectedListener
            sTime.setOnTouchListener(sTimeOnSelectedListener)
            tvImportance.setOnClickListener(tvSImportanceListener)
            tvDifficulty.setOnClickListener(tvSDifficultyListener)
        }

        override fun makeClickable() {
            sImportance.setEnabled(true)
            sDifficulty.setEnabled(true)
            etRowHeader.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS)
            etRowHeader.setFocusableInTouchMode(true)
            etRowHeader.setFocusable(true)
            if (etRowHeader.text!!.length > 0)
                etRowHeader.setSelection(etRowHeader.text!!.length - 1)
            sDate.setEnabled(true)
            sTime.setEnabled(true)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycleview_row, viewGroup, false)
        val viewHolder = ViewHolder(view)


        //viewHolder.
        //handleDeleteModeClicks(viewHolder)
        //viewHolder.setListeners()
        return viewHolder
    }
    /*fun addTask(task : TaskModel) {
        models.add(task)
        notifyDataSetChanged()
    }*/
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        handleDeleteModeClicks(viewHolder)
        updatePositions(viewHolder, position)
        //getActivity().showKeyboard(viewHolder.etRowHeader, getActivity())
        super.onBindViewHolder(viewHolder, position)

        handleFocus(viewHolder, position)
        //getActivity().hideKeyboard()
        //if (isRowAdded())
            //handleFocus(viewHolder, position)
        //TODO DOESNT WORK: tusim proc
        /*if (models[position].isNew()) {
            getActivity().showKeyboard(viewHolder.etRowHeader)
            models[position].setNew(false)
        }*/
        //handleFocus(viewHolder, position)
    }
    override fun updatePositions(viewHolder : ViewHolder, position: Int) {
        viewHolder.titleTextWatcher.updateModel(models[position])
        viewHolder.sDifficultyListener.updateModel(models[position])
        viewHolder.sImportanceListener.updatePosition(models[position])
        viewHolder.ibCheckOnClickListener.updatePosition(models[position])
        viewHolder.sDateOnSelectedListener.updatePosition(models[position])
        viewHolder.sTimeOnSelectedListener.updatePosition(models[position])
        super.updatePositions(viewHolder, position)
    }
    fun handleFocus(viewHolder : ViewHolder, position: Int) {
        if (position != models.size -1)
            return
        //TODO...
        val editText = viewHolder.etRowHeader
        if (isRowAdded()) {
            getActivity().showKeyboard(viewHolder.etRowHeader, getActivity())
            setRowAdded(false)
        }
    }
    /*fun makeClickable(viewHolder : ViewHolder) {
        viewHolder.sImportance.setEnabled(true)
        viewHolder.sDifficulty.setEnabled(true)
        //viewHolder.etRowHeader.setInputType(0)
        viewHolder.etRowHeader.setFocusable(true)
        viewHolder.sDate.setEnabled(true)
    }*/
    override fun setCheckCircle(viewHolder: ViewHolder, position: Int) {
        viewHolder.ibCheck.progress = 0
        if (models[position].isCompleted()) {
            viewHolder.ibCheck.setBackgroundResource(R.drawable.ic_checked)
            viewHolder.ibCheck.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.green))
        }
        else {
            viewHolder.ibCheck.setBackgroundResource(R.drawable.ic_unchecked)
            viewHolder.ibCheck.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.white))
        }
    }
    class TitleTextWatcher() : TextWatcher {
        private lateinit var task : TaskModel
        fun updateModel(new : TaskModel) {
            task = new
        }
        var position : Int = -1
        fun updatePosition(new : Int) {
            position = new
        }
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            task.setTitle(p0.toString())
        }

        override fun afterTextChanged(p0: Editable?) {

        }
    }
    inner class SDifficultyListener() : AdapterView.OnItemSelectedListener, View.OnTouchListener,
        ListenerCallback {
        private lateinit var task : TaskModel
        private var userTouch : Boolean = false
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            userTouch = true
            getActivity().hideKeyboard(getActivity())
            return false
        }
        override fun setTouch(newValue : Boolean) {
            userTouch = newValue
        }
        fun updateModel(new : TaskModel) {
            task = new
            userTouch = false
        }
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
            if (userTouch) {
                task.setDifficulty(position)
                notifyDataSetChanged()
                userTouch = false
            }
            /*Log.d("my_tag2", position.toString())
            Log.d("my_tag2", task.getDifficulty().toString())*/
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {
        }

    }
    inner class SImportanceListener() : AdapterView.OnItemSelectedListener, View.OnTouchListener,
        ListenerCallback {
        private lateinit var task : TaskModel
        private var userTouch : Boolean = false
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            userTouch = true
            getActivity().hideKeyboard(getActivity())
            return false
        }
        override fun setTouch(newValue : Boolean) {
            userTouch = newValue
        }
        fun updatePosition(newValue : TaskModel) {
            task = newValue
            userTouch = false
        }
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            if (userTouch) {
                task.setImportance(position)
                notifyDataSetChanged()
                userTouch = false
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

    }
    inner class IbCheckOnClickListener() : OnClickListener {
        private lateinit var task : TaskModel
        fun updatePosition(newValue: TaskModel) {
            task = newValue
        }
        override fun onClick(v: View?) {
            getActivity().hideKeyboard(getActivity())
            task.setCompleted(!task.isCompleted())
            notifyDataSetChanged()
            //v?.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.blue))
            //v?.setBackgroundTintList(ColorStateList.valueOf(ResourcesCompat.getColor(getActivity(), )))
        }
    }
    inner class SDateOnSelectedListener() : AdapterView.OnItemSelectedListener, View.OnTouchListener {
        private lateinit var task : TaskModel
        private var userTouch : Boolean = false
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            userTouch = true
            getActivity().hideKeyboard(getActivity())
            return false
        }
        fun updatePosition(newValue : TaskModel) {
            task = newValue
            userTouch = false
        }
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            //if (!view!!.isEnabled) return
            if (!userTouch) return
            when(position) {
                0 -> {
                    task.setDeadline(LocalDateTime.parse(LocalDateTime.MAX.toString()))
                    //notifyDataSetChanged()
                }
                2 -> {
                    task.setDeadline(LocalDateTime.parse(LocalDateTime.MIN.toString()))
                    //notifyDataSetChanged()
                }
                1 -> {
                    val dialog = DeadlineDialog(task, getActivity())
                    dialog.startDateDialog()
                }
            }
            userTouch = false
            notifyDataSetChanged()
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
        }
    }
    inner class STimeOnSelectedListener() : AdapterView.OnItemSelectedListener, View.OnTouchListener {
        private lateinit var task : TaskModel
        private var userTouch : Boolean = false
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            userTouch = true
            getActivity().hideKeyboard(getActivity())
            return false
        }
        fun updatePosition(newValue : TaskModel) {
            task = newValue
            userTouch = false
        }
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            if (!userTouch) return
            when(position) {
                0 -> {
                    task.setDeadline(task.getDeadline().toLocalDate().atTime(LocalTime.MAX))
                    //notifyDataSetChanged()
                }
                2 -> {
                    task.setDeadline(task.getDeadline().toLocalDate().atTime(LocalTime.MIN))
                    //notifyDataSetChanged()
                }
                1 -> {
                    val dialog = DeadlineDialog(task, getActivity())
                    dialog.startTimeDialog()
                }
            }
            userTouch = false
            notifyDataSetChanged()
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
        }
    }
    inner class TextViewSpinnerListener(private val spinner: Spinner, private val listener : ListenerCallback) : OnClickListener{
        override fun onClick(v: View?) {
            getActivity().hideKeyboard(getActivity())
            listener.setTouch(true)
            spinner.performClick()
        }

    }

}
interface ListenerCallback {
    fun setTouch(newValue: Boolean)
}