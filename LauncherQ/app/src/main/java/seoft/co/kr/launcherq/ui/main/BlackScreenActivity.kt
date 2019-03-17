package seoft.co.kr.launcherq.ui.main

import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.support.v4.view.GestureDetectorCompat
import android.support.v7.app.AppCompatActivity
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_black_screen.*
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.utill.i

class BlackScreenActivity : AppCompatActivity() {

    val TAG = "BlackScreenActivity#$#"

    private lateinit var gestureDetectorCompat : GestureDetectorCompat

    var defaultTurnOffTime = 0
    var defaultBrightness = 0

    var offCount = 0
    val GOAL_COUNT = 25
    val INTERVAL_TIME = 200L

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

        countingOff()

        llBlack.setOnClickListener { offCount = 0 }

        gestureDetectorCompat = GestureDetectorCompat(this, object: GestureDetector.SimpleOnGestureListener(){
            override fun onDoubleTap(e: MotionEvent?): Boolean {
                offCount = GOAL_COUNT - 1
                finish()
                return super.onDoubleTap(e)
            }
        })

        llBlack.setOnTouchListener { view, motionEvent ->
            gestureDetectorCompat.onTouchEvent(motionEvent)
            false
        }
    }

    fun countingOff(){
        Handler().postDelayed( {
            "$offCount".i(TAG)
            if(offCount >= GOAL_COUNT) {
                Settings.System.putInt(contentResolver,Settings.System.SCREEN_OFF_TIMEOUT, defaultTurnOffTime)
                Settings.System.putInt(contentResolver,Settings.System.SCREEN_BRIGHTNESS, defaultBrightness)
                "defaultTurnOffTime $defaultTurnOffTime   defaultBrightness $defaultBrightness   FINISH".i(TAG)
                finish()
            } else {
                offCount++
                countingOff()
            }
        },INTERVAL_TIME)
    }

    fun hideBars(){
        val options = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = options
        actionBar?.hide()
    }
}
