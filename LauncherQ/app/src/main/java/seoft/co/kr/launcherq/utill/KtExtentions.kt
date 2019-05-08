package seoft.co.kr.launcherq.utill

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.content.res.Resources
import android.databinding.ObservableField
import android.graphics.Color
import android.support.annotation.IdRes
import android.support.v7.app.ActionBar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.util.Log
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import seoft.co.kr.launcherq.data.model.WidgetInfoType
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

fun String.getWidget() : WidgetInfoType {
    return when(this) {
        WidgetInfoType.TIME.getStr -> WidgetInfoType.TIME
        WidgetInfoType.AMPM.getStr -> WidgetInfoType.AMPM
        WidgetInfoType.DATE.getStr -> WidgetInfoType.DATE
        WidgetInfoType.DOW.getStr -> WidgetInfoType.DOW
        WidgetInfoType.TEXT.getStr -> WidgetInfoType.TEXT
        else -> WidgetInfoType.ERR
    }
}

fun Int.getWidget() : WidgetInfoType {
    return when(this) {
        WidgetInfoType.TIME.getInt -> WidgetInfoType.TIME
        WidgetInfoType.AMPM.getInt -> WidgetInfoType.AMPM
        WidgetInfoType.DATE.getInt -> WidgetInfoType.DATE
        WidgetInfoType.DOW.getInt -> WidgetInfoType.DOW
        WidgetInfoType.TEXT.getInt -> WidgetInfoType.TEXT
        else -> WidgetInfoType.ERR
    }
}

fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)


// Int extentions

fun Int.toStrColor() = String.format("#%08X", this)

fun Int.toPixel() = (this * Resources.getSystem().displayMetrics.density).toInt()
fun Int.toDp() = (this / Resources.getSystem().displayMetrics.density).toInt()


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


fun AlertDialog.Builder.showDialogWithTwoInput(title:String? = null, message:String? = null,
                                            postiveBtText:String? = null, negativeBtText:String? = null,
                                            cbPostive : ((String)->Unit)? = null, cbNegative:(()->Unit)? = null,
                                               inputType1: Int, text1:String,hint1:String = "",
                                               inputType2: Int, text2:String,hint2:String ="") {
    title?.let {  setTitle(title) }
    message?.let { setMessage(message) }

    val etInput1 = EditText(context).apply {
        this.inputType = inputType1
        this.text = text1.toEditable()
        this.hint = hint1.toEditable()
    }
    val etInput2 = EditText(context).apply {
        this.inputType = inputType2
        this.text = text2.toEditable()
        this.hint = hint2.toEditable()
    }
    val ll = LinearLayout(context).apply { orientation = LinearLayout.VERTICAL }
    val lp = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT).apply { setMargins(55,0,55,0) }

    ll.addView(etInput1,lp)
    ll.addView(etInput2,lp)

    this.setView(ll)

    postiveBtText?.let { setPositiveButton(postiveBtText){
            _,_ -> cbPostive?.invoke("${etInput1.text}${SC.SPLITTER}${etInput2.text}")
    }}
    negativeBtText?.let { setNegativeButton(negativeBtText){
            _,_ -> cbNegative?.invoke()
    }}
    show()
}


fun Any.i(tag:String = "#$#") {
    Log.i(tag,this.toString())
}
