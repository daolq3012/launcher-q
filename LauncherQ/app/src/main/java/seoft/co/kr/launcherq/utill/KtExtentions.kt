package seoft.co.kr.launcherq.utill

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.databinding.ObservableField
import android.graphics.Color
import android.support.annotation.IdRes
import android.support.v7.app.ActionBar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import seoft.co.kr.launcherq.ui.MsgType
import seoft.co.kr.launcherq.ui.ViewModelHelper

operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
    add(disposable)
}

fun <T: Any> ObservableField<T>.value(): T = get()!!

fun ViewModelHelper.observeActMsg(lifecycleOwner: LifecycleOwner, observer: Observer<MsgType>){
    actMsg.observe(lifecycleOwner,observer)
}

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

fun String.toIntColor() = Color.parseColor(this)

// view

fun AppCompatActivity.setupActionBar(@IdRes toolbarId: Int, action: ActionBar.() -> Unit) {
    setSupportActionBar(findViewById(toolbarId))
    supportActionBar?.run {
        action()
    }
}

fun AppCompatActivity.replace(@IdRes frameId: Int, fragment: android.support.v4.app.Fragment, tag: String? = null) {
    supportFragmentManager.beginTransaction().replace(frameId, fragment, tag).commit()
}

// dialog
fun AlertDialog.Builder.showDialog(title:String? = null, message:String? = null,
                                   postiveBtText:String? = null, negativeBtText:String? = null,
                                   cbPostive : (()->Unit)? = null, cbNegative:(()->Unit)? = null) {
    title?.let {  setTitle(title) }
    message?.let { setMessage(message) }
    postiveBtText?.let { setPositiveButton(postiveBtText){
            _,_ -> cbPostive?.invoke()
    }}
    negativeBtText?.let { setNegativeButton(negativeBtText){
            _,_ -> cbNegative?.invoke()
    }}
    show()
}

fun AlertDialog.Builder.showDialogWithInput(title:String? = null, message:String? = null,
                                            postiveBtText:String? = null, negativeBtText:String? = null,
                                            cbPostive : ((String)->Unit)? = null, cbNegative:(()->Unit)? = null,
                                            inputType: Int) {
    title?.let {  setTitle(title) }
    message?.let { setMessage(message) }

    val etInput = EditText(context).apply { this.inputType = inputType }
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
