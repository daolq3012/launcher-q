package seoft.co.kr.launcherq.ui.main

import android.graphics.Point
import android.view.GestureDetector
import android.view.MotionEvent

// ref : http://ukzzang.tistory.com/45
class MainGestureListener(val screenSize: Point, val cb:(MainGestureListenerCmd)->Unit) : GestureDetector.SimpleOnGestureListener() {

    val TAG = "MainGestureListener#$#"

    override fun onLongPress(e: MotionEvent?) {
        cb.invoke(MainGestureListenerCmd.LONG_PRESS)
        super.onLongPress(e)
    }

    override fun onDoubleTap(e: MotionEvent?): Boolean {
        cb.invoke(MainGestureListenerCmd.DOUBLE_TAP)
        return super.onDoubleTap(e)
    }

    override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {

        /**
         * fling boundary : screenSize.y - screenSize.y / 10 * 1.5
         */
        if (e1.y > screenSize.y - screenSize.y / 10 * 1.5 && velocityY < 100) {
            cb.invoke(MainGestureListenerCmd.FLING_UP)
        }

        return super.onFling(e1, e2, velocityX, velocityY)
    }

    enum class MainGestureListenerCmd{
        LONG_PRESS,
        DOUBLE_TAP,
        FLING_UP,
    }

}