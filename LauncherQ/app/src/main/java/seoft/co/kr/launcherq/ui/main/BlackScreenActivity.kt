package seoft.co.kr.launcherq.ui.main

import android.os.Bundle
import android.provider.Settings
import android.support.v4.view.GestureDetectorCompat
import android.support.v7.app.AppCompatActivity
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import seoft.co.kr.launcherq.R

class BlackScreenActivity : AppCompatActivity() {

    val TAG = "BlackScreenActivity#$#"

    private lateinit var gestureDetectorCompat : GestureDetectorCompat

    var defaultTurnOffTime = 0
    var defaultBrightness = 0

    var onResumeCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_black_screen)

        defaultTurnOffTime = Settings.System.getInt(contentResolver, Settings.System.SCREEN_OFF_TIMEOUT, 30000)
        defaultBrightness = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, 150)

        hideBars()

        Settings.System.putInt(contentResolver, Settings.System.SCREEN_OFF_TIMEOUT, 1)
        Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, 0)

        gestureDetectorCompat = GestureDetectorCompat(this, object: GestureDetector.SimpleOnGestureListener(){
            override fun onDoubleTap(e: MotionEvent?): Boolean {
                finish()
                return super.onDoubleTap(e)
            }
        })
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetectorCompat.onTouchEvent(event)
        return true
    }


    fun hideBars(){
        val options = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = options
        actionBar?.hide()
    }

    override fun onResume() {
        onResumeCount++
        if(onResumeCount>=2) finish()
        super.onResume()
    }

    override fun onPause() {
        Settings.System.putInt(contentResolver,Settings.System.SCREEN_OFF_TIMEOUT, defaultTurnOffTime)
        Settings.System.putInt(contentResolver,Settings.System.SCREEN_BRIGHTNESS, defaultBrightness)
        super.onPause()
    }

}

//class BlackScreenActivity : AppCompatActivity() {
//
//    val TAG = "BlackScreenActivity#$#"
//
//    private lateinit var gestureDetectorCompat : GestureDetectorCompat
//
//    var defaultTurnOffTime = 0
//    var defaultBrightness = 0
//
//    var offCount = 0
//    val GOAL_COUNT = 10
//    val INTERVAL_TIME = 500L
//
//    var onResumeCount = 0
//    var isEnd = false
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN)
//
//        setContentView(R.layout.activity_black_screen)
//
//        defaultTurnOffTime = Settings.System.getInt(contentResolver, Settings.System.SCREEN_OFF_TIMEOUT, 30000)
//        defaultBrightness = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, 150)
//
//        hideBars()
//
//        Settings.System.putInt(contentResolver, Settings.System.SCREEN_OFF_TIMEOUT, 1)
//        Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, 0)
//
//        counting()
//
//        gestureDetectorCompat = GestureDetectorCompat(this, object: GestureDetector.SimpleOnGestureListener(){
//            override fun onDoubleTap(e: MotionEvent?): Boolean {
//                forceFinish()
//                return super.onDoubleTap(e)
//            }
//        })
//    }
//
//    override fun onTouchEvent(event: MotionEvent): Boolean {
//
//        if(event.action == MotionEvent.ACTION_MOVE) {
//            offCount=0
//        }
//        gestureDetectorCompat.onTouchEvent(event)
//        return true
//    }
//
//    fun forceFinish(){
//        isEnd = true
//        Settings.System.putInt(contentResolver,Settings.System.SCREEN_OFF_TIMEOUT, defaultTurnOffTime)
//        Settings.System.putInt(contentResolver,Settings.System.SCREEN_BRIGHTNESS, defaultBrightness)
//        finish()
//    }
//
//    fun counting(){
//        Handler().postDelayed( {
//            if(isEnd)return@postDelayed
//            if(offCount >= GOAL_COUNT)
//                forceFinish()
//            else {
//                offCount++
//                counting()
//            }
//        },INTERVAL_TIME)
//    }
//
//    fun hideBars(){
//        val options = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//        window.decorView.systemUiVisibility = options
//        actionBar?.hide()
//    }
//
//    override fun onResume() {
//        onResumeCount++
//        if(onResumeCount>=2) forceFinish()
//        super.onResume()
//    }
//
//}
