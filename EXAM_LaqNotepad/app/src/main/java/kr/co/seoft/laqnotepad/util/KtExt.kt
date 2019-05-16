package kr.co.seoft.laqnotepad.util

import android.support.annotation.IdRes
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.util.Log
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