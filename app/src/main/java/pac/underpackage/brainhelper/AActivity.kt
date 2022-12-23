package pac.underpackage.brainhelper

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.View.FOCUS_UP
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.*
import android.widget.EditText
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.postDelayed
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.delay

//import `package`.underpackage.brainhelper.R
//import com.example.brainhelper.R



fun View.focusAndShowKeyboard(tryAgain: Boolean = true) {
    /**
     * This is to be called when the window already has focus.
     */
    fun View.showTheKeyboardNow() {
        if (isFocused) {
            post {
                // We still post the call, just in case we are being notified of the windows focus
                // but InputMethodManager didn't get properly setup yet.
                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                val isInputMethodManagerSetup = imm.showSoftInput(this, SHOW_IMPLICIT)
                if (!isInputMethodManagerSetup) {
                    // InputMethodManager still didn't get properly setup yet even we post the call.
                    // so we should give it one more try.
                    if (tryAgain) {
                        this.focusAndShowKeyboard(false)
                    }
                }
            }
        }
    }
    requestFocus()
    if (hasWindowFocus()) {
        // No need to wait for the window to get focus.
        showTheKeyboardNow()
    } else {
        this@focusAndShowKeyboard.showTheKeyboardNow()
        return
        // We need to wait until the window gets focus.
        viewTreeObserver.addOnWindowFocusChangeListener(
            object : ViewTreeObserver.OnWindowFocusChangeListener {
                override fun onWindowFocusChanged(hasFocus: Boolean) {
                    // This notification will arrive just before the InputMethodManager gets set up.
                    //Log.d("my_tag5", windowToken.toString() + "NE" + hasWindowFocus().toString())
                    if (hasFocus) {
                        this@focusAndShowKeyboard.showTheKeyboardNow()
                        // It’s very important to remove this listener once we are done.
                        viewTreeObserver.removeOnWindowFocusChangeListener(this)
                    }
                }
            })
    }
}

fun View.focusAndHideKeyboard(tryAgain: Boolean = true) {
    /**
     * This is to be called when the window already has focus.
     */
    fun View.hideTheKeyboardNow() {
        if (isFocused) {
            post {
                // We still post the call, just in case we are being notified of the windows focus
                // but InputMethodManager didn't get properly setup yet.
                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                val isInputMethodManagerSetup = imm.hideSoftInputFromWindow(this.windowToken, 0)
                if (!isInputMethodManagerSetup) {
                    // InputMethodManager still didn't get properly setup yet even we post the call.
                    // so we should give it one more try.
                    if (tryAgain) {
                        this.focusAndHideKeyboard(false)
                    }
                }
            }
        }
    }

    //requestFocus()
    if (hasWindowFocus()) {
        // No need to wait for the window to get focus.
        hideTheKeyboardNow()
    } else {
        //this@focusAndHideKeyboard.hideTheKeyboardNow()
        //return
        // We need to wait until the window gets focus.
        viewTreeObserver.addOnWindowFocusChangeListener(
            object : ViewTreeObserver.OnWindowFocusChangeListener {
                override fun onWindowFocusChanged(hasFocus: Boolean) {
                    // This notification will arrive just before the InputMethodManager gets set up.
                    if (hasFocus) {
                        this@focusAndHideKeyboard.hideTheKeyboardNow()
                        // It’s very important to remove this listener once we are done.
                        viewTreeObserver.removeOnWindowFocusChangeListener(this)
                    }
                }
            })
    }
}


abstract class AActivity() : AppCompatActivity() {
    protected lateinit var onBackPressedCallback : OnBackPressedCallback
    fun getOnBackPressedCallBack() = onBackPressedCallback
    abstract fun getRV() : RecyclerView
    protected lateinit var dummyFocus : androidx.constraintlayout.widget.ConstraintLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aactivity)
    }
    fun <T : AActivity>hideKeyboard(activity: T) {
        val view = activity.currentFocus
        //val view = dummyFocus
        if (view == null) return
        view.focusAndHideKeyboard()
        view?.clearFocus()
        //Log.d("my_tag5", activity.toString() + " hide " + view?.toString())
        //Log.d("my_tag5", view?.hasFocus().toString())
        //view.requestFocus()
        return
        //if (view == null) return
        Thread.sleep(1)
        val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        Thread.sleep(1)
        //inputMethodManager.showSoftInput(view, 0)
        //view = dummyFocus
        //dummyFocus.requestFocus()
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
        Thread.sleep(1)
        //view?.clearFocus()
        Thread.sleep(1)
        Log.d("my_tag5", view?.hasFocus().toString())
        //view.clearFocus()
        Log.d("my_tag5", view?.hasFocus().toString())

        return
        //view?.setFocusableInTouchMode(false)
        //view?.setFocusableInTouchMode(true)
        //view?.setFocusable(false)
        //dummyFocus.visibility = View.VISIBLE
        //dummyFocus.post{dummyFocus.requestFocus()}
        //dummyFocus.requestFocus()
        //dummyFocus.clearFocus()
        //dummyFocus.clearFocus()
        //dummyFocus.visibility = View.GONE
        //view.clearFocus()
        /*view.clearFocus()
        view.visibility = View.GONE
        view.visibility = View.VISIBLE*/
        //dummyFocus.setFocusable(true)
        //dummyFocus.setFocusableInTouchMode(true)
        //dummyFocus.post {dummyFocus.requestFocus()}
        //var view = activity.getCurrentFocus()

        //view?.focusAndHideKeyboard()
        //view?.clearFocus()
        return
        //view = dummyFocus
        //dummyFocus.clearFocus()
        //view?.clearFocus()
        //dummyFocus.postDelayed ({dummyFocus.requestFocus() }, 100)
        /*dummyFocus.post ( object : Runnable{
            override fun run() {
                dummyFocus.requestFocus()
            }
        })*/

        //view?.setFocusable(true)
        //view?.setFocusableInTouchMode(true)
        //inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY)
        //inputMethodManager.hideSoftInputFromWindow(view?.windowToken, InputMethodManager.HIDE_IMPLICIT_ONLY)
        //inputMethodManager.hideSoftInputFromWindow(view?.windowToken, InputMethodManager.HIDE_IMPLICIT_ONLY)
        view?.clearFocus()

    }
    fun <T : AActivity>showKeyboard(view: View?, activity : T) {
        //view?.requestFocusFromTouch()
        //
        //val view = dummyFocus
        //Log.d("my_tag5", activity.toString() + " show " + view?.toString())
        //Log.d("my_tag5", view?.hasFocus().toString())
        //dummyFocus.requestFocus()
        //hideKeyboard(activity)
        //view?.requestFocus()
        //Thread.sleep(1)
        //view?.focusAndHideKeyboard()

        hideKeyboard(activity)
        //Log.d("my_tag5", "before focus")
        view?.focusAndShowKeyboard()
        //view?.focusAndShowKeyboard()
        //view2?.requestFocus()
        //Log.d("my_tag5", activity.toString() + " show " + view?.toString())
        //Log.d("my_tag5", view?.hasFocus().toString())
        return
        //hideKeyboard(activity)
        //view?.setFocusable(true)
        //view?.requestFocus()
        //getSystemService(Context.INPUT_METHOD_SERVICE)
        val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        //TODO WHEN NULL
        //inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED)
        inputMethodManager.showSoftInput(view, 0)
        //inputMethodManager.toggleSoftInput(SHOW_IMPLICIT, HIDE_IMPLICIT_ONLY)
        return
        //inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
        //if (activity.resources.configuration.hardKeyboardHidden == Context.ke)

        //inputMethodManager.showSoftInput(view, 0)

        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        //view.performClick()
        //view?.requestFocus()
    }
}