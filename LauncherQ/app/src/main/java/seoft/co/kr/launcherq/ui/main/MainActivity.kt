package seoft.co.kr.launcherq.ui.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.graphics.Point
import android.os.Bundle
import android.support.v4.view.GestureDetectorCompat
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.activity_main.*
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.databinding.ActivityMainBinding
import seoft.co.kr.launcherq.ui.MsgType
import seoft.co.kr.launcherq.ui.main.RequestManager.Companion.REQ_PERMISSIONS
import seoft.co.kr.launcherq.utill.SC
import seoft.co.kr.launcherq.utill.observeActMsg
import seoft.co.kr.launcherq.utill.toPixel


class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity#$#"

    private lateinit var binding:ActivityMainBinding
    private lateinit var vm : MainViewModel
    private lateinit var requestManager : RequestManager
    private lateinit var gestureDetectorCompat : GestureDetectorCompat
    private val screenSize = Point()

    private val NONE = 0
    private val TOUCH_START = 1
    private val OPEN_ONE = 2
    private val OPEN_TWO = 3


    private val timeReceiver :  TimeReceiver by lazy {
        TimeReceiver()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        vm = ViewModelProviders.of(this, MainViewModel(Repo).create()).get(MainViewModel::class.java)
        binding.vm = vm
        binding.executePendingBindings()

        vm.observeActMsg(this, Observer {
            when(it) {
                MsgType.START_ACTIVITY -> startActivity(Intent(applicationContext,vm.msg as Class<*>))

            }
        })

        inits()
        requestManager = RequestManager(this)

    }

    override fun onResume() {
        super.onResume()


        if(SC.needResetBgSetting)
            resetSettingAct()
        else
            vm.resetBgWidgets() // when onresume but don't need setting reset, example) quit another app, clock refresh

        registerReceiver(timeReceiver, IntentFilter().apply {
            addAction(Intent.ACTION_TIME_TICK)
        })

    }

    override fun onStop() {
        super.onStop()

        unregisterReceiver(timeReceiver)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQ_PERMISSIONS && grantResults.all {it == PackageManager.PERMISSION_GRANTED}) {
            vm.start()
        } else {
            requestManager.showPermissionRequestDialog()
        }
    }

    fun inits(){
        windowManager.defaultDisplay.getRealSize( screenSize )
        gestureDetectorCompat = GestureDetectorCompat(this,MainGestureListener(this,screenSize))
    }

    var startX = 0
    var startY = 0
    var distance = 150
    var boundary = 50
    var step = NONE
    var oneStepSize = 200

    var coordinates = arrayOf(
        arrayOf(Point(0,0),Point(0,0)),
        arrayOf(Point(0,0),Point(0,0)),
        arrayOf(Point(0,0),Point(0,0)),
        arrayOf(Point(0,0),Point(0,0))
    )

    override fun onTouchEvent(event: MotionEvent): Boolean {

        if(event.action == MotionEvent.ACTION_DOWN) {

            var optX = 0
            var optY = 0

            startX = event.x.toInt()
            startY = event.y.toInt()

            if(startX + distance.toPixel() > screenSize.x ) optX = startX + distance.toPixel() - screenSize.x
            else if(startX - distance.toPixel() < 0 ) optX = startX - distance.toPixel()

            if(startY + distance.toPixel() > screenSize.y ) optY = startY + distance.toPixel() - screenSize.y
            else if(startY - distance.toPixel() < 0 ) optY = startY - distance.toPixel()

            val params = RelativeLayout.LayoutParams(distance.toPixel()*2, distance.toPixel()*2)
                .apply { setMargins(startX - distance.toPixel() - optX, startY - distance.toPixel() - optY,0,0) }

            val rstMidX = startX - optX
            val rstMidY = startY - optY

            rlAppStarter.layoutParams = params

            val rstNormalBoundary = boundary.toPixel()
            val rstLargeBoundary = boundary.toPixel()*2
            val rstDistance = distance.toPixel()

            // *2 는 위치에 따라 가로 혹은 세로 범위를 늘려 접근률을 높이기 위함
            coordinates[0].run {
                this[0].x = rstMidX - rstLargeBoundary
                this[0].y = rstMidY - rstDistance - rstNormalBoundary
                this[1].x = rstMidX + rstLargeBoundary
                this[1].y = rstMidY - rstDistance + rstNormalBoundary
            }
            coordinates[1].run {
                this[0].x = rstMidX + rstDistance - rstNormalBoundary
                this[0].y = rstMidY - rstLargeBoundary
                this[1].x = rstMidX + rstDistance + rstNormalBoundary
                this[1].y = rstMidY + rstLargeBoundary
            }
            coordinates[2].run {
                this[0].x = rstMidX - rstLargeBoundary
                this[0].y = rstMidY + rstDistance - rstNormalBoundary
                this[1].x = rstMidX + rstLargeBoundary
                this[1].y = rstMidY + rstDistance + rstNormalBoundary
            }
            coordinates[3].run {
                this[0].x = rstMidX - rstDistance - rstNormalBoundary
                this[0].y = rstMidY - rstLargeBoundary
                this[1].x = rstMidX - rstDistance + rstNormalBoundary
                this[1].y = rstMidY + rstLargeBoundary
            }

            rlAppStarter.visibility = View.VISIBLE
            step = TOUCH_START

        } else if(event.action == MotionEvent.ACTION_MOVE) {

            if(step == TOUCH_START) {

                val curX = event.x.toInt()
                val curY = event.y.toInt()

                with(coordinates) {
                    for (i in 0 until 4) {
                        if (this[i][0].x < curX && curX < this[i][1].x &&
                            this[i][0].y < curY && curY < this[i][1].y
                        ) {
                            var optX = 0
                            var optY = 0

                            val oneStartX = event.x.toInt()
                            val oneStartY = event.y.toInt()

                            if(oneStartX + oneStepSize.toPixel()/2 > screenSize.x ) optX = oneStartX + oneStepSize.toPixel()/2 - screenSize.x
                            else if(oneStartX - oneStepSize.toPixel()/2 < 0 ) optX = oneStartX - oneStepSize.toPixel()/2

                            if(oneStartY + oneStepSize.toPixel()/2 > screenSize.y ) optY = oneStartY + oneStepSize.toPixel()/2 - screenSize.y
                            else if(oneStartY - oneStepSize.toPixel()/2 < 0 ) optY = oneStartY - oneStepSize.toPixel()/2

                            val params = RelativeLayout.LayoutParams(oneStepSize.toPixel(), oneStepSize.toPixel())
                                .apply { setMargins(oneStartX - oneStepSize.toPixel()/2 - optX, oneStartY - oneStepSize.toPixel()/2 - optY,0,0) }
                            gvApps.layoutParams = params


                            gvApps.visibility = View.VISIBLE
                            rlAppStarter.visibility = View.INVISIBLE
                            step = OPEN_ONE
                        }
                    }
                }

            } else if(step == OPEN_ONE) {

            }


        } else if(event.action == MotionEvent.ACTION_UP) {

            rlAppStarter.visibility = View.INVISIBLE
        }










        gestureDetectorCompat.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    fun showSettingInMainDialog(){
        val simd = SettingMainEntranceDialog(this){}
        simd.show()

    }

    // call when called resetSettingInVM in ViewModel
    fun resetSettingAct(){
        vm.setDeviceXY(screenSize.x,screenSize.y)
        vm.resetBgBitmap()
        vm.resetBgWidgets()
        SC.needResetBgSetting = false

    }

    inner class TimeReceiver : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.action.run {
                if(this == Intent.ACTION_TIME_TICK) vm.resetBgWidgets()
            }
        }

    }

}
