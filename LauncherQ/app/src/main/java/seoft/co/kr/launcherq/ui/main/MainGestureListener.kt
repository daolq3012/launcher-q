package seoft.co.kr.launcherq.ui.main

import android.graphics.Point
import android.view.GestureDetector
import android.view.MotionEvent

// ref : http://ukzzang.tistory.com/45
class MainGestureListener(val activity: MainActivity, val screenSize:Point) : GestureDetector.SimpleOnGestureListener() {

    val TAG = "MainGestureListener#$#"

    override fun onLongPress(e: MotionEvent?) {
        activity.showSettingInMainDialog()

        super.onLongPress(e)
    }

    override fun onDoubleTap(e: MotionEvent?): Boolean {
        return super.onDoubleTap(e)
    }

//    override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
//
//        if( e1.y > screenSize.y - screenSize.y/10*1.5 && velocityY < 100){
//            activity.startActivity(
//                Intent(activity.applicationContext, DrawerActivity::class.java)
//            )
//            activity.overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
//
//        }
//
//        return super.onFling(e1, e2, velocityX, velocityY)
//    }


}