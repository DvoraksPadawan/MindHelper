package pac.underpackage.brainhelper.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pac.underpackage.brainhelper.MainActivity
import pac.underpackage.brainhelper.Models.ToDoListModel
import pac.underpackage.brainhelper.R
//import com.example.brainhelper.R
import pac.underpackage.brainhelper.TasksActivity

class ToDoListsAdapter(models: ArrayList<ToDoListModel>, activity: MainActivity) :
        AAdapter<ToDoListModel, ToDoListsAdapter.ViewHolder, MainActivity>(models, activity) {

    inner class ViewHolder(view: View) : AViewHolder(view, getActivity()) {
        val onRowClickListener : OnRowClickListener = OnRowClickListener()


        //val onTitleClickListener = OnRowClickListener()
        //val onTitleLongClickListener = OnRowLongClickListener()
        //val listener = initOnItemClickListener()
        /*val listener = object : View.OnClickListener {
                var position = 0
                fun updatePosition(aPosition: Int) {
                    position = aPosition
                }
                override fun onClick(p0: View?) {
                    val intent = Intent(context, TasksActivity()::class.java)
                    intent.putExtra("toDoListID", models[position].getID())
                    intent.putExtra("isTemplate", (context as MainActivity).isTemplate())
                    intent.putExtra("toDoListCreated", true)
                    //val intent = Intent(this, TasksActivity()::class.java)
                    context.startActivity(intent)
                }
            }*/
        init {

        }
        override fun setListeners() {
            if (!bDeleteCheck.hasOnClickListeners()) return
            super.setListeners()
            itemView.setOnClickListener(onRowClickListener)
            etRowHeader.setOnClickListener(onRowClickListener)
            ibCheck.setOnClickListener(onRowClickListener)
            tvDifficulty.setOnClickListener(onRowClickListener)
            tvImportance.setOnClickListener(onRowClickListener)
            tvCountdown.setOnClickListener(onRowClickListener)
            //sDate.setOnClickListener(onRowClickListener)
        }

        override fun makeClickable() {
            //BECAUSE TODOLISTVIEW IS NEVER "CLICKABLE":
            makeUnclicable()
        }

    }

    override fun setCheckCircle(viewHolder: ViewHolder, position: Int) {
        viewHolder.ibCheck.setBackgroundResource(0)
        //viewHolder.ibCheck.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.green))
        //viewHolder.ibCheck.setIndeterminateTintList(ContextCompat.getColorStateList(getActivity(), R.color.green))
        viewHolder.ibCheck.progress = models[position].getCompleted()
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycleview_row, viewGroup, false)
        //TODO NEVIM JESTLI TU VAL CI VAR
        val viewHolder = ViewHolder(view)
        //handleDeleteModeClicks(viewHolder)
        //viewHolder.setListeners()
        return viewHolder
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        handleDeleteModeClicks(viewHolder)
        updatePositions(viewHolder, position)
        super.onBindViewHolder(viewHolder, position)
    }

    override fun updatePositions(viewHolder : ViewHolder, position: Int) {
        viewHolder.onRowClickListener.updatePosition(models[position])
        super.updatePositions(viewHolder, position)
        //TODO just voluntary:
        //viewHolder.onTitleClickListener.updatePosition(models[position])
        //viewHolder.onTitleLongClickListener.updatePosition(models[position])
    }
    fun initOnItemClickListener() : View.OnClickListener {
        return (object : View.OnClickListener {
            var position = 0
            fun updatePosition(aPosition: Int) {
                position = aPosition
            }
            override fun onClick(p0: View?) {
                //val intent = Intent(context, TasksActivity()::class.java)
                //intent.putExtra("toDoListID", models[position].getID())
                //intent.putExtra("isTemplate", (context as MainActivity).isTemplate())
                //intent.putExtra("toDoListCreated", true)
                //val intent = Intent(this, TasksActivity()::class.java)
                //context.startActivity(intent)
            }
        })
    }


    inner class OnRowClickListener() : View.OnClickListener {
        //private lateinit var context: Context
        private var ID = 0
        private lateinit var toDoList : ToDoListModel
        fun updatePosition(newValue : ToDoListModel) {
            toDoList = newValue
            ID = toDoList.getID()
            //context = aContext
        }
        //TODO NA CHVILI FALSE
        override fun onClick(p0: View?) {
            val intent = Intent(getActivity(), TasksActivity()::class.java)
            intent.putExtra("toDoListID", ID)
            //intent.putExtra("isTemplate",(context as MainActivity).isTemplate())
            //intent.putExtra("calledFromTemplate",(context as MainActivity).isTemplate())
            intent.putExtra("isTemplate", getActivity().isTemplate())
            intent.putExtra("calledFromTemplate",getActivity().isTemplate())
            intent.putExtra("toDoListCreated", true)
            //val intent = Intent(this, TasksActivity()::class.java)
            getActivity().startActivity(intent)
            //startActivity(intent)
        }
    }
}