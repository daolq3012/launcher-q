package seoft.co.kr.launcherq.ui.main

import android.graphics.Point
import android.view.GestureDetector
import android.view.MotionEvent

// ref : http://ukzzang.tistory.com/45
class MainGestureListener(val activity: MainActivity,val cb:(MainGestureListenerCmd)->Unit) : GestureDetector.SimpleOnGestureListener() {

    val TAG = "MainGestureListener#$#"

    override fun onLongPress(e: MotionEvent?) {
        cb.invoke(MainGestureListenerCmd.LONG_PRESS)
        super.onLongPress(e)
    }

    override fun onDoubleTap(e: MotionEvent?): Boolean {
        cb.invoke(MainGestureListenerCmd.DOUBLE_TAP)
        return super.onDoubleTap(e)
    }

    enum class MainGestureListenerCmd{
        LONG_PRESS,
        DOUBLE_TAP
    }

}