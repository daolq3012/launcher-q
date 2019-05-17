package kr.co.seoft.laqnotepad.util

import android.support.annotation.IdRes
import android.support.v7.app.ActionBar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.util.Log
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import java.util.*

// String extentions
fun String.i(tag:String = "#$#") {
    Log.i(tag,this)
}

fun String.e(tag:String = "#$#") {
    Log.e(tag,this)
}

fun String.toast( duration: Int = Toast.LENGTH_LONG): Toast {
    return Toast.makeText(App.get, this, duration).apply { show()  }
}

fun String.toasti(tag:String = "#$#", duration: Int = Toast.LENGTH_LONG): Toast {
    this.i(tag)
    return Toast.makeText(App.get, this, duration).apply { show()  }
}

fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

// Any extentions
fun Any.i(tag:String = "#$#") {
    Log.i(tag,this.toString())
}

// view
fun AppCompatActivity.setupActionBar(@IdRes toolbarId: Int, action: ActionBar.() -> Unit) {
    setSupportActionBar(findViewById(toolbarId))
    supportActionBar?.run {
        action()
    }
}

fun Int.TO_STRING():String{
    return App.get.resources.getString(this)
}

fun Date.toCalendar() : Calendar{
    return GregorianCalendar().also { it.time = this }
}

fun Calendar.toYYYYMMDD() : String {
    return with(this){ "${get(Calendar.YEAR)}. ${get(Calendar.MONTH)}. ${get(Calendar.DATE)}" }
}


fun AlertDialog.Builder.showDialogWithInput(title:String? = null, message:String? = null,
                                            postiveBtText:String? = null, negativeBtText:String? = null,
                                            cbPostive : ((String)->Unit)? = null, cbNegative:(()->Unit)? = null,
                                            inputType: Int, text:String) {
    title?.let {  setTitle(title) }
    message?.let { setMessage(message) }

    val etInput = EditText(context).apply {
        this.inputType = inputType
        this.text = text.toEditable()
    }
    val ll = LinearLayout(context).apply { orientation = LinearLayout.VERTICAL }
    val lp = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT).apply { setMargins(55,0,55,0) }

    ll.addView(etInput,lp)

    this.setView(ll)

    postiveBtText?.let { setPositiveButton(postiveBtText){
            _,_ -> cbPostive?.invoke(etInput.text.toString())
    }}
    negativeBtText?.let { setNegativeButton(negativeBtText){
            _,_ -> cbNegative?.invoke()
    }}
    show()
}