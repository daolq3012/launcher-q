package seoft.co.kr.launcherq.ui.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.*
import android.content.pm.LauncherApps
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.graphics.Point
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Process
import android.support.v4.view.GestureDetectorCompat
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.activity_main.*
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.data.model.*
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

    val LQ_DATA = "LQ_DATA"
    val TIME_INTERVAL = 200L

    var curPosInOneStep = -1
    var befPosInOneStep = -1
    var curPosKeepCnt = 0

    var twoStepStartPos = Point()

    companion object {
        val launcherApps : LauncherApps by lazy { App.get.getSystemService(Context.LAUNCHER_APPS_SERVICE) as LauncherApps }
    }

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
            vm.gridItemSize,
            vm.lastestDir
        ){
            if(it.isLongClick)
                when (it.quickApp.type) {

                    QuickAppType.ONE_APP -> if(getShortcutFromApp(it.quickApp.commonApp.pkgName).isNotEmpty()) openTwoStep(it.quickApp, true)
                    QuickAppType.FOLDER, QuickAppType.TWO_APP -> openTwoStep(it.quickApp, true)
                    QuickAppType.EXPERT -> {
                        if(it.quickApp.expert!!.useTwo == null) openArrangeActivityWithDir()
                        else openTwoStep(it.quickApp, true)
                    }
                    else -> openArrangeActivityWithDir()
                }
            else
                runOneStepApp(it.quickApp)
        }
    }

    fun openArrangeActivityWithDir() {
        val intent = Intent(applicationContext, ArrangeActivity::class.java)
            .apply { putExtra(ArrangeActivity.DIR,vm.lastestDir) }
        startActivity(intent)
    }


    fun getShortcutFromApp(pkgName: String) : List<Shortcut> {

        val shortcutQuery = LauncherApps.ShortcutQuery().apply {
            setQueryFlags(
                LauncherApps.ShortcutQuery.FLAG_MATCH_DYNAMIC or
                LauncherApps.ShortcutQuery.FLAG_MATCH_MANIFEST or
                LauncherApps.ShortcutQuery.FLAG_MATCH_PINNED)
            setPackage(pkgName)
        }

        return try {
            launcherApps.getShortcuts(shortcutQuery,Process.myUserHandle())?.map {
                Shortcut(it.id, it.`package`, it.shortLabel.toString(), it)
            } ?: emptyList()
        } catch ( e:java.lang.Exception) {
            emptyList()
        }
    }

    fun runOneStepApp(quickApp: QuickApp) {
        when(quickApp.type){
            QuickAppType.ONE_APP -> {

                if(quickApp.commonApp.isExcept) {
                    when(quickApp.commonApp.pkgName) {
                        CAppException.DRAWER.get -> startActivity( Intent(applicationContext, DrawerActivity::class.java) )
//                        CAppException.CALL.get -> startActivity( Intent(Intent.ACTION_DIAL,null))
                    }

                } else {
                    "quickApp.commonApp.pkgName : ${quickApp.commonApp.pkgName}".i(TAG)
                    "quickApp.commonApp.detailName : ${quickApp.commonApp.detailName}".i(TAG)
                    launchApp(quickApp.commonApp.pkgName)
                }

                vm.step.set(Step.NONE)

            }
            QuickAppType.FOLDER -> {
                openTwoStep(quickApp)
            }
            QuickAppType.EXPERT -> {
                if(quickApp.commonApp.pkgName != "" && quickApp.commonApp.detailName != "")
                    launchApp(quickApp.commonApp.pkgName)
                else if(quickApp.expert!!.useOne != null) {
                    runExpertApp(quickApp.expert!!.useOne!!)
                }
                vm.step.set(Step.NONE)
            }
            else -> {}
        }
    }


    private fun runExpertApp(customIntent: CustomIntent) {

        if(customIntent.checkLaunchDefaultMethod()) {
            startActivity(packageManager.getLaunchIntentForPackage(customIntent.pkgName))
            return
        }

        try {
            val tmpIntent = Intent().apply {
                action = customIntent.action
                customIntent.uriData?.let { data = Uri.parse(it) }
                customIntent.type?.let { type = it }
                customIntent.categorys.forEach { addCategory(it) }
                intent.setFlags(customIntent.flag)
                customIntent.addFlag?.run {
                    forEach { addFlags(it) }
                }
                customIntent.pkgName?.let { `package` = it }
                customIntent.customComponentName?.let {
                    component = ComponentName(it.compName,it.compCls)
                }
            }
            startActivity(tmpIntent)

        } catch (e:Exception) {

            "############# CATCH EXCEPTION #############".i(TAG)
            e.toString().i(TAG)
            e.printStackTrace().i(TAG)
            "###########################################".i(TAG)

            e.toString().toast()
        }

    }

    fun launchApp(pkgName:String){
        startActivity(packageManager.getLaunchIntentForPackage(pkgName))
    }

    fun launchAppFromOptions(pkgName:String, detailName:String, data: String = ""){
        val compname = ComponentName(pkgName, detailName)
        val actintent = Intent(Intent.ACTION_MAIN)
            .apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                component = compname
                putExtra(LQ_DATA,data)
            }
        applicationContext.startActivity(actintent)
    }

    /**
     * call this method after first open two step view
     *
     * we can use two step when type is FOLDER, TWO_APP, EXPERT
     * else type never call this method
     */
    private fun runTwoStepApp(pos:Int) {
        "runTwoStepApp pos : $pos".i(TAG)

        with(vm.twoStepApp.value()) {
            when (type) {

                QuickAppType.ONE_APP -> {
                    val shortCutApp = getShortcutFromApp(vm.twoStepApp.value().commonApp.pkgName)[pos]
                    launcherApps.startShortcut(shortCutApp.packageName, shortCutApp.id, null, null, Process.myUserHandle())
                }
                QuickAppType.FOLDER, QuickAppType.TWO_APP -> {
                    cmds[pos].i(TAG)
                    val cApp = cmds[pos].toCommonApp()
                    launchApp(cApp.pkgName)
                }
                QuickAppType.EXPERT -> {
                    expert!!.useTwo!![pos]!!.i(TAG)
                    runExpertApp(expert!!.useTwo!![pos]!!)
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
     *
     * but, item was updated in databinding apater
     *
     * using shortcut when quickApp was pure ONE_APP type
     *
     * block expert don't have useTwo(is null) when before process
     */
    fun openTwoStep(quickApp: QuickApp, isLongClick :Boolean = false) {
        vm.twoStepApp.set(quickApp)
        vm.step.set(Step.OPEN_TWO)

        var twoStepItemCnt = 0

        when (quickApp.type) {
            QuickAppType.FOLDER, QuickAppType.TWO_APP -> twoStepItemCnt = quickApp.cmds.size
            QuickAppType.EXPERT -> twoStepItemCnt = quickApp.expert!!.useTwo!!.count { it != null }
            QuickAppType.ONE_APP -> twoStepItemCnt = getShortcutFromApp(quickApp.commonApp.pkgName).size
        }

        if(twoStepItemCnt == 0) {
            "Two step is empty".toast()
            vm.step.set(Step.NONE)
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
        gestureDetectorCompat = GestureDetectorCompat(this,MainGestureListener(this){
            when(it) {
                MainGestureListener.MainGestureListenerCmd.LONG_PRESS -> { if (vm.step != Step.OPEN_ONE) showSettingInMainDialog() }
                MainGestureListener.MainGestureListenerCmd.DOUBLE_TAP -> {}
            }
        })
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

                if(vm.liveDataApps.value!![curPosInOneStep].type == QuickAppType.ONE_APP && getShortcutFromApp(vm.liveDataApps.value!![curPosInOneStep].commonApp.pkgName).isEmpty()) {
                    intervalStart()
                    return@postDelayed
                } else if(vm.liveDataApps.value!![curPosInOneStep].type == QuickAppType.EXPERT &&
                    vm.liveDataApps.value!![curPosInOneStep].expert!!.useTwo == null) {
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

        // TEST/////////////////

//        val URL = "content://${LQProvider.PROVIDER_NAME}"
//
//        val students = Uri.parse(URL)
//
//        val cursor = contentResolver.query(students, null, null, null, null)
//
//        cursor?.run {
//            if(moveToFirst()) {
//                while(true){
//                    "${getString(getColumnIndex(LQProvider._ID))}  ${getString(getColumnIndex(LQProvider.PKG_NAME))}  ${getString(getColumnIndex(LQProvider.INFO))}".i(TAG)
//                    "${getString(getColumnIndex(LQProvider._ID))}  ${getString(getColumnIndex(LQProvider.PKG_NAME))}  ${getString(getColumnIndex(LQProvider.INFO))}".toast()
//                    if(!moveToNext()) break
//                }
//            }
//        }
//        cursor?.close()

//        val cr = contentResolver
//        val uri =  cr.query(LQProvider.CONTENT_URI,ContentValues())

//        1. 컨텐트 프로바이더 LQ 꺼 재대로짜고 LQ내에서 CRUD 함 하고 (메인수준) 서브플젝도 하기(메인수준)
//        2. LQ꺼 프로바이더 REPO로 빼고 서브플젝은 그냥 객채화 잘하기
//        3. 라이브러리화 하기

//        val k = uri.pathSegments
//        k.get(0)








        ////////////////////










//        launchAppFromOptions("seoft.co.kr.twostepexample","seoft.co.kr.twostepexample.AActivity","data")

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
