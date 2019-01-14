package seoft.co.kr.launcherq.ui.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.*
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
import seoft.co.kr.launcherq.data.model.QuickApp
import seoft.co.kr.launcherq.data.model.QuickAppType
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
    private val mc = MainCaculator()

    private val NONE = 0
    private val TOUCH_START = 1
    private val OPEN_ONE = 2
    private val OPEN_TWO = 3

    var step = NONE

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

        vm.liveDataApps.observe(this,
            Observer {
                it?.let {
                    val gridCnt = vm.gridCnt
                    gvApps.numColumns = gridCnt
                    gvApps.adapter = MainGridAdapter(
                        this,
                        it.take(gridCnt * gridCnt).toMutableList(),
                        vm.gridItemSize
                        ){
                        runApp(it)
                    }
                }
            })

        inits()
        requestManager = RequestManager(this)

    }


    /**
     * 앱 터치/때기 -> 실행
     * 앱 웨잇 -> 2뎁스 실행
     * 서랍 터치/때기/웨잇 -> 서랍열기 ( 2뎁스와 같은 UI? )
     */

    fun runApp(quickApp: QuickApp) {
        when(quickApp.type){
            QuickAppType.ONE_APP -> {
                val compname = ComponentName(quickApp.commonApp.pkgName, quickApp.commonApp.detailName)
                val actintent = Intent(Intent.ACTION_MAIN)
                    .apply {
                        addCategory(Intent.CATEGORY_LAUNCHER)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                        component = compname
                    }
                applicationContext.startActivity(actintent)
            }
            QuickAppType.FOLDER -> { }

        }
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

    override fun onTouchEvent(event: MotionEvent): Boolean {

        if(event.action == MotionEvent.ACTION_DOWN) {

           val distance = vm.distance

            mc.calcOpenTouchStart(event.x.toInt(),event.y.toInt(), distance, screenSize)
            val params = RelativeLayout.LayoutParams(distance.toPixel()*2, distance.toPixel()*2)
                .apply { setMargins(mc.startViewMarginPointX, mc.startViewMarginPointY,0,0) }

            rlAppStarter.layoutParams = params

            rlAppStarter.visibility = View.VISIBLE
            gvApps.visibility = View.INVISIBLE
            step = TOUCH_START

        } else if(event.action == MotionEvent.ACTION_MOVE) {

            val gvSize = vm.gridViewSize

            if(step == TOUCH_START) {


                val curX = event.x.toInt()
                val curY = event.y.toInt()

                // check cur touch in boundary
                with(mc.coordinates) {
                    for (i in 0 until 4) {
                        if (this[i][0].x < curX && curX < this[i][1].x &&
                            this[i][0].y < curY && curY < this[i][1].y ) {

                            mc.calcOpenOneStep(curX, curY,gvSize,screenSize)

                            val params = RelativeLayout.LayoutParams(gvSize.toPixel(), gvSize.toPixel())
                                .apply { setMargins(mc.gridViewMarginPointX, mc.gridViewMarginPointY,0,0) }
                            gvApps.layoutParams = params
                            gvApps.visibility = View.VISIBLE

                            vm.setAppsFromDir(i)

                            rlAppStarter.visibility = View.INVISIBLE
                            step = OPEN_ONE
                        }
                    }
                }

            } else if(step == OPEN_ONE) {



            } else if(step == OPEN_TWO) {



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
    // TODO need to add function of resetGridValue()
    fun resetSettingAct(){
        vm.setDeviceXY(screenSize.x,screenSize.y)
        vm.resetBgBitmap()
        vm.resetBgWidgets()
        vm.resetGridValue()
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
