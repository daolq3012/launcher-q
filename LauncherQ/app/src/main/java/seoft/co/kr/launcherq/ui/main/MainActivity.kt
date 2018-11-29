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
import android.view.WindowManager
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.databinding.ActivityMainBinding
import seoft.co.kr.launcherq.ui.MsgType
import seoft.co.kr.launcherq.ui.main.RequestManager.Companion.REQ_PERMISSIONS
import seoft.co.kr.launcherq.utill.SC
import seoft.co.kr.launcherq.utill.observeActMsg


class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity#$#"

    private lateinit var binding:ActivityMainBinding
    private lateinit var vm : MainViewModel
    private lateinit var requestManager : RequestManager
    private lateinit var gestureDetectorCompat : GestureDetectorCompat
    private val screenSize = Point()


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

        if(SC.needResetSetting)
            resetSettingAct()

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
        gestureDetectorCompat = GestureDetectorCompat(this,GestureListener(this,screenSize))
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        gestureDetectorCompat.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    fun showSettingInMainDialog(){
        val simd = SettingEntranceDialog(this){}
        simd.show()

    }

    // call when called resetSettingInVM in ViewModel
    fun resetSettingAct(){
        vm.setDeviceXY(screenSize.x,screenSize.y)
        vm.resetBgBitmap()
        vm.resetBgWidgets()
        SC.needResetSetting = false

    }

    inner class TimeReceiver : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.action.run {
                if(this == Intent.ACTION_TIME_TICK) {
                    vm.resetBgWidgets()
                }
            }
        }

    }




}
