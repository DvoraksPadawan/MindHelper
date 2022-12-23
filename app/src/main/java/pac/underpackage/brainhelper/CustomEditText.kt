package pac.underpackage.brainhelper

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.widget.EditText
import androidx.activity.OnBackPressedCallback

class CustomEditText(context : Context, attrs : AttributeSet) : androidx.appcompat.widget.AppCompatEditText(context, attrs) {
    private lateinit var onBackPressedCallback : OnBackPressedCallback
    fun addListener(listener : OnBackPressedCallback) {
        onBackPressedCallback = listener
    }
    override fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressedCallback.handleOnBackPressed()
            return false
        }
        return super.onKeyPreIme(keyCode, event)
    }
}