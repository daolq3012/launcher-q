package seoft.co.kr.twostepexample

import android.util.Log
import android.widget.Toast


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
