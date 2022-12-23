package pac.underpackage.brainhelper.Adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.*


class SpinnerAdapter<S>(context : Context, layout : Int, options : MutableList<S>) : ArrayAdapter<S>(context, layout, options) {
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var itemView : View? = null
        /*if (position == 0) {
            itemView.setBackgroundColor(activity.getColor(R.color.white))
        }
        if (position == 1) {
            itemView.setBackgroundColor(activity.getColor(R.color.green))
        }
        if (position == 2) {
            itemView.setBackgroundColor(activity.getColor(R.color.blue))
        }
        if (position == 3) {
            itemView.setBackgroundColor(activity.getColor(R.color.red))
        }*/
        if (position == 3) {
            val textView = TextView(context)
            textView.visibility = View.GONE
            textView.height = 0
            itemView = textView
        }
        else
            itemView = super.getDropDownView(position, convertView, parent)
        return itemView
    }
}
