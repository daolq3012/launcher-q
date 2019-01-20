package seoft.co.kr.launcherq.ui.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.*
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.graphics.Point
import android.os.Bundle
import android.os.Handler
import android.support.v4.view.GestureDetectorCompat
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.activity_main.*
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.data.model.CAppException
import seoft.co.kr.launcherq.data.model.QuickApp
import seoft.co.kr.launcherq.data.model.QuickAppType
import seoft.co.kr.launcherq.databinding.ActivityMainBinding
import seoft.co.kr.launcherq.ui.MsgType
import seoft.co.kr.launcherq.ui.arrange.ArrangeActivity
import seoft.co.kr.launcherq.ui.drawer.DrawerActivity
import seoft.co.kr.launcherq.ui.main.RequestManager.Companion.REQ_PERMISSIONS
import seoft.co.kr.launcherq.utill.*


class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity#$#"

    private lateinit var binding:ActivityMainBinding
    private lateinit var vm : MainViewModel
    private lateinit var requestManager : RequestManager
    private lateinit var gestureDetectorCompat : GestureDetectorCompat
    private val screenSize = Point()
    private val mc = MainCaculator()

    val TIME_INTERVAL = 200L

    var curPosInOneStep = -1
    var befPosInOneStep = -1
    var curPosKeepCnt = 0

    var twoStepStartPos = Point()

    private val timeReceiver :  TimeReceiver by lazy { TimeReceiver() }

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
                MsgType.PICK_TWO_STEP_ITEM -> runTwoStepApp(vm.msg as Int)
                else -> {}
            }
        })

        vm.liveDataApps.observe(this, Observer { it?.let { refreshGrid(it) } })

        inits()
        requestManager = RequestManager(this)

    }

    fun refreshGrid(quickApps : MutableList<QuickApp>){
        val gridCnt = vm.gridCnt
        gvApps.numColumns = gridCnt
        gvApps.adapter = MainGridAdapter(
            this,
            quickApps.take(gridCnt * gridCnt).toMutableList(),
            vm.gridItemSize
        ){
            if(it.isLongClick)
                when (it.quickApp.type) {
                    QuickAppType.EXPERT, QuickAppType.FOLDER, QuickAppType.TWO_APP -> openTwoStep(it.quickApp, true)
                    else -> {
                        val intent = Intent(applicationContext, ArrangeActivity::class.java)
                            .apply { putExtra(ArrangeActivity.DIR,vm.lastestDir) }

                        startActivity(intent)
                    }
                }
            else
                runOneStepApp(it.quickApp)
        }
    }


    fun runOneStepApp(quickApp: QuickApp) {
        when(quickApp.type){
            QuickAppType.ONE_APP -> {

                if(quickApp.commonApp.isExcept) {
                    when(quickApp.commonApp.pkgName) {
                        CAppException.DRAWER.get -> startActivity( Intent(applicationContext, DrawerActivity::class.java) )
                        CAppException.CALL.get -> startActivity( Intent(Intent.ACTION_DIAL,null))
                    }

                } else {
                    val compname = ComponentName(quickApp.commonApp.pkgName, quickApp.commonApp.detailName)
                    val actintent = Intent(Intent.ACTION_MAIN)
                        .apply {
                            addCategory(Intent.CATEGORY_LAUNCHER)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                            component = compname
                        }
                    applicationContext.startActivity(actintent)
                }
                vm.step.set(Step.NONE)

            }
            QuickAppType.FOLDER -> {
                openTwoStep(quickApp)
            }

        }
    }

    /**
     * call this method after first open two step view
     *
     * we can use two step when type is FOLDER, TWO_APP, EXPERT
     * else type never call this method
     */
    private fun runTwoStepApp(pos:Int) {

        with(vm.twoStepApp.value()) {
            when (type) {
                QuickAppType.FOLDER, QuickAppType.TWO_APP -> {
                    cmds[pos].i(TAG)
                }
                QuickAppType.EXPERT -> {
                    expert!!.useTwo[pos].toString().i(TAG)
                }
                else ->{}
            }
        }

        clearViews()
    }

    /**
     * open two step view use this method
     * set visibilty use update Observable value [twoStepApp] and auto update view using databinding
     * if isLongClick -> open position is using before gridview margin [mc.gridViewMarginPointX]
     * default Click -> open position is detach finger point position, is updating in onTouch listener value is [marginPoint]
     *
     * first, check two step item count
     * second, calc height using item count
     * third, adjust size to view
     */
    fun openTwoStep(quickApp: QuickApp, isLongClick :Boolean = false) {
        vm.twoStepApp.set(quickApp)
        vm.step.set(Step.OPEN_TWO)

        var twoStepItemCnt = 0

        when (quickApp.type) {
            QuickAppType.FOLDER, QuickAppType.TWO_APP -> twoStepItemCnt = quickApp.cmds.size
            QuickAppType.EXPERT -> twoStepItemCnt = quickApp.expert!!.useTwo.size
        }

        if(twoStepItemCnt == 0) {
            //TODO empty 예외처리
        }

        // need same to dp value from xml view
        val width = resources.getDimension(R.dimen.two_step_bg_width).toInt()
        val height = resources.getDimension(R.dimen.two_step_item_height).toInt() * twoStepItemCnt

        val marginPoint = mc.calcOpenTwoStep(twoStepStartPos.x, twoStepStartPos.y, screenSize, width, height)
        val params = RelativeLayout.LayoutParams(width, height)
            .apply { setMargins(
                if(isLongClick) mc.gridViewMarginPointX else marginPoint.x,
                if(isLongClick) mc.gridViewMarginPointY else marginPoint.y,
                0,0) }

        llTwoStepBg.layoutParams = params
    }

    override fun onResume() {
        super.onResume()

        vm.step.set(Step.NONE)
        vm.emptyTwoStepApp()

        if(SC.needResetBgSetting) resetBgSetting() // reset whole properties
        else vm.resetBgWidgets() // when onresume but don't need setting reset, example) quit another app, clock refresh

        if(SC.needResetUxSetting) resetUxSetting()

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

            vm.emptyTwoStepApp()

           val distance = vm.distance

            mc.calcOpenTouchStart(event.x.toInt(),event.y.toInt(), distance, screenSize)
            val params = RelativeLayout.LayoutParams(distance.toPixel()*2, distance.toPixel()*2)
                .apply { setMargins(mc.startViewMarginPointX, mc.startViewMarginPointY,0,0) }

            rlAppStarter.layoutParams = params

            vm.step.set(Step.TOUCH_START)

        } else if(event.action == MotionEvent.ACTION_MOVE) {

            val gvSize = vm.gridViewSize

            if(vm.step.value() == Step.TOUCH_START) {

                val curX = event.x.toInt()
                val curY = event.y.toInt()

                // check cur touch in boundary
                with(mc.starterCoordinates) {
                    for (i in 0 until 4) {
                        if (this[i][0].x < curX && curX < this[i][1].x &&
                            this[i][0].y < curY && curY < this[i][1].y ) {

                            mc.calcOpenOneStep(curX, curY,gvSize,screenSize,vm.gridCnt)

                            val params = RelativeLayout.LayoutParams(gvSize.toPixel(), gvSize.toPixel())
                                .apply { setMargins(mc.gridViewMarginPointX, mc.gridViewMarginPointY,0,0) }
                            gvApps.layoutParams = params
                            vm.setAppsFromDir(i)
                            vm.step.set(Step.OPEN_ONE)
                            intervalStart()
                        }
                    }
                }

            } else if(vm.step.value() == Step.OPEN_ONE) {
                twoStepStartPos.x = event.x.toInt()
                twoStepStartPos.y = event.y.toInt()
                curPosInOneStep = mc.calcInboundOneStep(twoStepStartPos.x,twoStepStartPos.y,vm.gridCnt)
            }
        } else if(event.action == MotionEvent.ACTION_UP) {
            if(vm.step.value() == Step.TOUCH_START) { vm.step.set(Step.NONE) }
            else if(vm.step.value() == Step.OPEN_ONE) {
                val pos = mc.calcInboundOneStep(event.x.toInt(),event.y.toInt(),vm.gridCnt)
                if(pos != -1 && vm.liveDataApps.value!![pos].type != QuickAppType.EMPTY)  {
                    runOneStepApp(vm.liveDataApps.value!![pos])
                }
            }

        }

        gestureDetectorCompat.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    fun intervalStart(){

        curPosInOneStep = -1
        befPosInOneStep = -1
        curPosKeepCnt = 0

        intervaling()
    }

    fun intervaling(){
        Handler().postDelayed({

            if(vm.step.value() != Step.OPEN_ONE) return@postDelayed

            if(curPosInOneStep == -1 || vm.liveDataApps.value!![curPosInOneStep].type == QuickAppType.EMPTY) {
                intervalStart()
                return@postDelayed
            }

            if(befPosInOneStep != curPosInOneStep) curPosKeepCnt = 0

            if(curPosKeepCnt >= vm.twoStepOpenInterval){

                if(vm.liveDataApps.value!![curPosInOneStep].type == QuickAppType.ONE_APP) {
                    intervalStart()
                    return@postDelayed
                }

                openTwoStep(vm.liveDataApps.value!![curPosInOneStep])

                return@postDelayed
            }

            curPosKeepCnt++

            befPosInOneStep = curPosInOneStep
            intervaling()
        },TIME_INTERVAL)
    }


    fun showSettingInMainDialog(){
        clearViews()
        val simd = SettingMainEntranceDialog(this){}
        simd.show()
    }

    // call when called resetSettingInVM in ViewModel
    fun resetBgSetting(){
        vm.setDeviceXY(screenSize.x,screenSize.y)
        vm.resetBgBitmap()
        vm.resetBgWidgets()
        SC.needResetBgSetting = false
    }

    fun resetUxSetting(){
        vm.resetUxValue()
        SC.needResetUxSetting = false
    }

    inner class TimeReceiver : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.action.run {
                if(this == Intent.ACTION_TIME_TICK) vm.resetBgWidgets()
            }
        }
    }

    override fun onBackPressed() {
        clearViews()
    }

    fun clearViews(){
        vm.step.set(Step.NONE)
        vm.emptyTwoStepApp()
    }

}
