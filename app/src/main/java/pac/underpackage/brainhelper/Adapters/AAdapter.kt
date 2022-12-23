package pac.underpackage.brainhelper.Adapters

import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import pac.underpackage.brainhelper.*
//import com.example.brainhelper.*
import pac.underpackage.brainhelper.Models.Model
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

abstract class AAdapter<T : Model, T2 : AAdapter<T, T2, T3>.AViewHolder, T3 : AActivity>(protected var models: ArrayList<T>,
                                                                                         private val activity : T3) : RecyclerView.Adapter<T2>(),
    Callback {

    abstract inner class AViewHolder(view : View, private val activity : T3) : RecyclerView.ViewHolder(view) {
        val etRowHeader : CustomEditText = view.findViewById(R.id.etRowTitle)
        val ibCheck : ProgressBar = view.findViewById(R.id.ibCheck)
        val sDifficulty : Spinner = view.findViewById(R.id.sDifficulty)
        val sImportance : Spinner = view.findViewById(R.id.sImportance)
        val bDeleteCheck : ImageButton = view.findViewById(R.id.bDeleteCheck)
        val sDate : Spinner = view.findViewById(R.id.sDate)
        val sTime : Spinner = view.findViewById(R.id.sTime)
        //val tvDateTime : TextView = view.findViewById(R.id.tvDateTime)

        //voluntary:
        val tvDifficulty : TextView = view.findViewById(R.id.tvDifficulty)
        val tvImportance : TextView = view.findViewById(R.id.tvImportance)
        lateinit var sDateOptions : ArrayList<String>
        lateinit var sDateAdapter : SpinnerAdapter<String>
        lateinit var sTimeOptions : ArrayList<String>
        lateinit var sTimeAdapter : SpinnerAdapter<String>
        lateinit var sDifficultyAdapter : ArrayAdapter<CharSequence>
        lateinit var sImportanceAdapter : ArrayAdapter<CharSequence>


        val onRowLongClickListener = OnRowLongClickListener()
        val onCheckBoxListener = OnCheckBoxListener()
        val tvCountdown : TextView = view.findViewById(R.id.tvCountdown)
        //var options2 = 0
        //val sImportance : Spinner = view.findViewById(R.id.)
        //TODO TOTO SMAZAT:
        //public val tvID : TextView = view.findViewById(R.id.tvID)
        //val rvRow : ItemView = view.findViewById(R.id.rvRow)
        init {
            initSpinnerAdapters(view)
            etRowHeader.addListener(getActivity().getOnBackPressedCallBack())
            when (isDeleteMode()) {
                true -> bDeleteCheck.setOnClickListener(null)
                false -> bDeleteCheck.setOnClickListener(onCheckBoxListener)
            }
        }


        fun initSpinnerAdapters(view: View) {
            val options = R.array.spinnerOptions
            //val adapter = ArrayAdapter.createFromResource(itemView.context, options, R.layout.my_spinner_layout)
            //adapter.setDropDownViewResource(R.layout.my_spinner_layout)
            //sDifficulty.adapter = adapter
            sDifficultyAdapter = ArrayAdapter.createFromResource(itemView.context, options, R.layout.spinner_layout_small)
            sDifficultyAdapter.setDropDownViewResource(R.layout.spinner_layout_align_center)
            sDifficulty.adapter = sDifficultyAdapter
            //val options2 = R.array.spinnerOptions
            //val adapter2 = ArrayAdapter.createFromResource(itemView.context, options2, R.layout.my_spinner_layout)
            //adapter2.setDropDownViewResource(R.layout.my_spinner_layout)
            //sImportance.adapter = adapter2
            sImportanceAdapter = ArrayAdapter.createFromResource(itemView.context, options, R.layout.spinner_layout_small)
            sImportanceAdapter.setDropDownViewResource(R.layout.spinner_layout_align_center)
            sImportance.adapter = sImportanceAdapter

            sDateOptions = activity.resources.getStringArray(R.array.spinnerDateOptions).toCollection(ArrayList())
            sDateAdapter = SpinnerAdapter<String>(itemView.context, R.layout.my_spinner_layout, this.sDateOptions)
            sDateAdapter.setDropDownViewResource(R.layout.spinner_layout_align_center)
            sDate.adapter = sDateAdapter

            sTimeOptions = activity.resources.getStringArray(R.array.spinnerTimeOptions).toCollection(ArrayList())
            sTimeAdapter = SpinnerAdapter(itemView.context, R.layout.my_spinner_layout, this.sTimeOptions)
            sTimeAdapter.setDropDownViewResource(R.layout.spinner_layout_align_center)
            sTime.adapter = sTimeAdapter
            //sDateOptions.add(LocalDateTime.MAX.toString())
        }
        open fun setListeners() {
            //Log.d("my_tag5", bDeleteCheck.hasOnClickListeners().toString())
            bDeleteCheck.setOnClickListener(null)
            itemView.setOnLongClickListener(onRowLongClickListener)
            etRowHeader.setOnLongClickListener(onRowLongClickListener)
            ibCheck.setOnLongClickListener(onRowLongClickListener)
            tvDifficulty.setOnLongClickListener(onRowLongClickListener)
            tvImportance.setOnLongClickListener(onRowLongClickListener)
            tvCountdown.setOnLongClickListener(onRowLongClickListener)
        }

        fun setDeleterListeners() {
            //Log.d("my_tag5", bDeleteCheck.hasOnClickListeners().toString())
            if (bDeleteCheck.hasOnClickListeners()) return
            bDeleteCheck.setOnClickListener(onCheckBoxListener)
            itemView.setOnClickListener(onCheckBoxListener)
            etRowHeader.setOnClickListener(onCheckBoxListener)
            etRowHeader.setOnLongClickListener(onRowLongClickListener)
            ibCheck.setOnClickListener(onCheckBoxListener)
            tvDifficulty.setOnClickListener(onCheckBoxListener)
            tvImportance.setOnClickListener(onCheckBoxListener)
            tvCountdown.setOnClickListener(onCheckBoxListener)
        }
        fun makeUnclicable() {
            sImportance.setEnabled(false)
            sDifficulty.setEnabled(false)
            etRowHeader.setFocusable(false)
            sDate.setEnabled(false)
            sTime.setEnabled(false)
            etRowHeader.setInputType(0)
        }
        abstract fun makeClickable()

    }
    open fun updatePositions(viewHolder : T2, position: Int) {
        viewHolder.onRowLongClickListener.updatePosition(models[position])
        if (viewHolder.bDeleteCheck.hasOnClickListeners())
            viewHolder.onCheckBoxListener.updatePosition(models[position])
    }
    private var deleteMode : Boolean = false
    private val selectedModels = ArrayList<T>()
    fun addModel(model: T, newID : Int = -1, newModel : Boolean = false) {
        if (newID > -1) model.setID(newID)
        /*if (model is TaskModel)
            if (new)
                model.setNew(true)*/
        models.add(model)
        model.setCallback(this)
        if (newModel) {
            setRowAdded(true)
            getActivity().getRV().scrollToPosition(models.size - 1)
        }
        notifyDataSetChanged()
    }
    fun addModels(newModels : ArrayList<T>, newID : Int = -1) {
        for (model in newModels) {
            addModel(model, newID)
        }
        //doesnt have to be called, cause tasks are sorted in onPause(), when saving into db
        //sort()
        //notifyDataSetChanged()
    }
    fun isDeleteMode() = deleteMode
    fun setDeleteMode(newValue : Boolean) {
        deleteMode = newValue
        //setModeChanged(true)
    }
    fun getActivity() = activity
    //private var modeChanged = true
    //fun isModeChanged() = modeChanged
    /*fun setModeChanged(newValue: Boolean) {
        modeChanged = newValue
    }*/
    private var rowAdded = false
    fun isRowAdded() = rowAdded
    fun setRowAdded(newValue: Boolean) {
        rowAdded = newValue
    }

    override fun onBindViewHolder(viewHolder: T2, position: Int) {
        //Log.d("my_tag2", "onSuperBind")
        //Log.d("my_tag3", "color" + viewHolder.itemView.getBackgroundTintList().toString())
        Log.d("my_tag3", "color" + viewHolder.tvDifficulty.getBackgroundTintList().toString())
        viewHolder.etRowHeader.setText(models[position].getTitle())

        viewHolder.sDifficulty.setSelection(models[position].getDifficulty())
        viewHolder.sDifficultyAdapter.notifyDataSetChanged()
        viewHolder.sImportance.setSelection(models[position].getImportance())
        viewHolder.sDifficultyAdapter.notifyDataSetChanged()
        setCheckBox(viewHolder, position)
        setCheckCircle(viewHolder, position)
        //TODO:
        //viewHolder.tvDateTime.text = models[position].getDeadline().toString()
        //TODO THIS WILL WORK ONLY FOR FIRST ROW

        //this two funs have to be behind handleDeleteModeClicks()

        setDate(viewHolder, position)
        setTime(viewHolder, position)

        viewHolder.tvCountdown.text = setCountdown(viewHolder, position)

        //if (modeChanged)
        //handleDeleteModeClicks(viewHolder)
        /*if (position == models.size -1)
            setModeChanged(false)*/
    }
    override fun getItemCount() = models.size

    fun setDate(viewHolder: T2, position: Int) {
        if (viewHolder.sDateOptions.size == 4) viewHolder.sDateOptions.removeLast()
        when(models[position].getDeadline()) {
            LocalDateTime.MAX -> viewHolder.sDate.setSelection(0)
            LocalDateTime.MIN -> viewHolder.sDate.setSelection(2)
            else -> {
                val formatter = DateTimeFormatter.ofPattern("dd.MM.uuuu")
                val output = models[position].getDeadline().format(formatter)
                //viewHolder.sDateOptions.add(models[position].getDeadline().toString())
                viewHolder.sDateOptions.add(output)
                viewHolder.sDate.setSelection(3)
            }
        }
        viewHolder.sDateAdapter.notifyDataSetChanged()
    }

    fun setTime(viewHolder: T2, position: Int) {
        if (viewHolder.sTimeOptions.size == 4) viewHolder.sTimeOptions.removeLast()
        when(models[position].getDeadline()) {
            LocalDateTime.MAX -> {
                viewHolder.sTime.setSelection(0)
                viewHolder.sTime.setEnabled(false)
            }
            LocalDateTime.MIN -> {
                viewHolder.sTime.setSelection(2)
                viewHolder.sTime.setEnabled(false)
            }
            else -> {
                val formatter = DateTimeFormatter.ofPattern("HH:mm")
                val output = models[position].getDeadline().format(formatter)
                //viewHolder.sTimeOptions.add(models[position].getDeadline().toString())
                viewHolder.sTimeOptions.add(output)
                viewHolder.sTime.setSelection(3)
            }
        }
        viewHolder.sTimeAdapter.notifyDataSetChanged()
    }
    fun setCountdown(viewHolder : T2, position : Int) : String {
        val deadline = models[position].getDeadline()
        if (deadline == LocalDateTime.MIN) return "ASAP"
        if (deadline == LocalDateTime.MAX) return "afap"
        var output = ""
        val countdown = Duration.between(LocalDateTime.now(), deadline)
        output = setCountdown(countdown.toDays().toInt()/365, "year")
        if (output != "") return output
        output = setCountdown(countdown.toDays().toInt()/30, "month")
        if (output != "") return output
        output = setCountdown(countdown.toDays().toInt(), "day")
        if (output != "") return output
        output = setCountdown(countdown.toHours().toInt(), "hour")
        if (output != "") return output
        output = setCountdown(countdown.toMinutes().toInt(), "min")
        if (output != "") return output
        /*if (output != "") return output
        output = setCountdownDays(remaining.toDays().toInt())
        if (output != "") return output
        output = setCountdownHours(remaining.toHours().toInt())
        if (output != "") return output*/
        if (countdown.toMillis() > 0 )
            output = "in a while"
        return output


    }
    fun setCountdown(remaining : Int, timeUnit : String) : String {
        var output = ""
        when {
            remaining == 1 -> output = "in $remaining $timeUnit"
            remaining == -1 -> output = "${remaining * -1} $timeUnit ago"
            remaining > 0 -> output = "in $remaining ${timeUnit + "s"}"
            remaining < 0 -> output = "${remaining * -1} ${timeUnit + "s"} ago"
        }
        return output
    }

    /*fun setCountdownHours(remainingHours : Int) : String {
        var output = ""
        when {
            remainingHours == 1 -> output = "in ${remainingHours} hour"
            remainingHours == -1 -> output = "${remainingHours * -1} hour ago"
            remainingHours > 0 -> output = "in ${remainingHours} hours"
            remainingHours < 0 -> output = "${remainingHours * -1} hours ago"
        }
        return output
    }
    fun setCountdownDays(remainingDays : Int) : String {
        var output = ""
        when {
            remainingDays == 1 -> output = "in ${remainingDays} day"
            remainingDays == -1 -> output = "${remainingDays * -1} day ago"
            remainingDays > 0 -> output = "in ${remainingDays} days"
            remainingDays < 0 -> output = "${remainingDays * -1} days ago"
        }
        return output
    }*/
    /*fun setCOuntdownDays(remainingDays : Int) : String {
        var output = ""
        if (remainingDays != 0) {
            output += remainingDays.toString()
            output += " day"
            if (remainingDays > 1) output += "s"
        }
        return output
    }
    fun setCountdownHours(remainingHours : Int) : String {
        var output = ""
        if (remainingHours != 0) {
            output += remainingHours.toString()
            output += " hour"
            if (remainingHours > 1) output += "s"
        }
        return  output
    }*/

    fun handleDeleteModeClicks(viewHolder : T2) {
        //Log.d("my_tag5", "listeners")
        if (isDeleteMode()) {
            viewHolder.makeUnclicable()
            viewHolder.setDeleterListeners()
            //TODO WHY???
            //viewHolder.setListeners()
        }
        else {
            viewHolder.setListeners()
            viewHolder.makeClickable()
        }
    }



    fun refreshData(newData : ArrayList<T>) {
        models = newData
        sort()
        notifyDataSetChanged()
    }
    fun getData() : ArrayList<T> {
        return models
    }
    fun setCheckBox(viewHolder: T2, position: Int) {
        if (!isDeleteMode()) {
            viewHolder.bDeleteCheck.visibility = View.INVISIBLE
            viewHolder.bDeleteCheck.setBackgroundResource(R.drawable.ic_vector_check_box_blank)
            models[position].setSelected(false)
        }
        else {
            viewHolder.bDeleteCheck.visibility = View.VISIBLE
            if (models[position].isSelected()) viewHolder.bDeleteCheck.setBackgroundResource(R.drawable.ic_vector_check_box)
            else viewHolder.bDeleteCheck.setBackgroundResource(R.drawable.ic_vector_check_box_blank)
        }
    }
    abstract fun setCheckCircle(viewHolder: T2, position: Int)
    fun getSelectedModels() : ArrayList<T> {
        return selectedModels
    }
    fun removeSelctedModels() {

    }
    inner class OnRowLongClickListener() : View.OnLongClickListener {
        private lateinit var model : T
        fun updatePosition(newValue : T) {
            model = newValue
        }
        override fun onLongClick(v: View?): Boolean {
            if (!isDeleteMode()) {
                setDeleteMode(true)
                (activity as Connector).startDeleteMode()
                selectedModels.clear()
                model.setSelected(true)
            }
            else model.setSelected(!model.isSelected())
            if (model.isSelected()) selectedModels.add(model)
            else selectedModels.remove(model)
            notifyDataSetChanged()
            return true
        }
    }
    inner class OnCheckBoxListener() : View.OnClickListener {
        private lateinit var model : T
        fun updatePosition(newValue : T) {
            model = newValue
        }
        override fun onClick(v: View?) {
            if (!isDeleteMode()) return
            model.setSelected(!model.isSelected())
            if (model.isSelected()) selectedModels.add(model)
            else selectedModels.remove(model)
            notifyDataSetChanged()
        }

    }

    override fun makeCallback() {
        notifyDataSetChanged()
    }
    fun sort(choice : Int = 0) {
        when (choice) {
            //0 -> models.sortWith(compareBy<T> {it.getDeadline()} .thenByDescending { it.getImportance() })
            0 -> {
                val dtComparator = DateTimeComparator<T>()
                val finalComparator = dtComparator.thenBy( {it.getImportance()})
                models.sortWith(finalComparator)
            }
            1 -> models.sortWith(compareBy( {it.getDifficulty()}))
            2 -> models.sortWith(compareByDescending( {it.getImportance()}))
        }
        notifyDataSetChanged()
    }
    fun selectAll() {
        for (model in models) {
            model.setSelected(true)
            selectedModels.add(model)
        }
        notifyDataSetChanged()
    }
}

