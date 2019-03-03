package seoft.co.kr.launcherq.ui.main

import android.graphics.Point
import android.view.GestureDetector
import android.view.MotionEvent
import seoft.co.kr.launcherq.utill.SC

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
         * 1. bottom to up fling boundary : screenSize.y - screenSize.y / 10 * 1.5
         * 2. up to bottom fling boundary : screenSize.y - screenSize.y / 10 * 1.5
         */
        if (e1.y > screenSize.y - screenSize.y / 10 * (SC.FLING_BOTTOM_BOUNDARY/10F) && velocityY < 100) {
            cb.invoke(MainGestureListenerCmd.FLING_UP)
        } else if (e1.y < screenSize.y / 10 * (SC.FLING_TOP_BOUNDARY/10F) && velocityY > 100) {
            cb.invoke(MainGestureListenerCmd.FLING_DOWN)
        }

        return super.onFling(e1, e2, velocityX, velocityY)
    }

    enum class MainGestureListenerCmd{
        LONG_PRESS,
        DOUBLE_TAP,
        FLING_UP,
        FLING_DOWN,
    }

}