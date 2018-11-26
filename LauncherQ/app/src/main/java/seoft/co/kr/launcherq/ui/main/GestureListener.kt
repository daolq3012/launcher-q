package seoft.co.kr.launcherq.ui.main

import android.view.GestureDetector
import android.view.MotionEvent
import kotlinx.android.synthetic.main.activity_main.*

// ref : http://ukzzang.tistory.com/45
class GestureListener(val activity: MainActivity) : GestureDetector.SimpleOnGestureListener() {

    override fun onLongPress(e: MotionEvent?) {
        activity.showSettingInMainDialog()

        super.onLongPress(e)
    }

}