package seoft.co.kr.launcherq.ui.main

import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import android.content.pm.PackageManager.MATCH_ALL
import android.databinding.ObservableField
import android.graphics.Bitmap
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.data.model.*
import seoft.co.kr.launcherq.ui.MsgType
import seoft.co.kr.launcherq.ui.ViewModelHelper
import seoft.co.kr.launcherq.utill.App
import seoft.co.kr.launcherq.utill.SC
import seoft.co.kr.launcherq.utill.toPixel

class MainViewModel(val repo: Repo): ViewModelHelper() {

    val TAG = "MainViewModel#$#"

    val bgBitmap : ObservableField<Bitmap> by lazy { SC.bgBitmap = repo.backgroundRepo.loadBitmap()
        ObservableField(SC.bgBitmap!!)
    }
    val bgwi : ObservableField<BackgroundWidgetInfos> by lazy { ObservableField(repo.preference.getBgWidgetInfos()) }

    val useTime : ObservableField<Boolean> by lazy { ObservableField(repo.preference.getBgTimeUse()) }
    val useAmpm : ObservableField<Boolean> by lazy { ObservableField(repo.preference.getBgAmpmUse()) }
    val useDate : ObservableField<Boolean> by lazy { ObservableField(repo.preference.getBgDateUse()) }
    val useDow : ObservableField<Boolean> by lazy { ObservableField(repo.preference.getBgDowUse()) }
    val useText : ObservableField<Boolean> by lazy { ObservableField(repo.preference.getBgTextUse()) }

    var liveDataApps = MutableLiveData<MutableList<QuickApp>>()

    val twoStepApp = ObservableField<QuickApp>(SC.EMPTY_QUICK_APP)

    var twoAppList = emptyList<Command>() // set when call openTwoStep in mainActivity

    val step = ObservableField<Step>(Step.NONE)
    var lastestDir = 0
    var gridItemSize = 0

    // loading property
    var gridCnt = 0
    var gridViewSize = 0
    var distance = 0 // = square of 1/2
    var twoStepOpenInterval = MutableLiveData<Int>() // 1 is 200ms

    override fun start() {

        repo.preference.run {
            if(isFirstLaunch()) {
                appInit()
                setIsFirst(false)
            }
        }

        resetUxValue()
        setImageCache()
        SC.drawerApps = repo.preference.getDrawerApps()
    }

    fun setImageCache(){

        repo.imageCacheRepo.setAll()


    }



    fun emptyTwoStepApp(){ twoStepApp.set(SC.EMPTY_QUICK_APP) }

    fun getTwoAppLaunchListAndSet(pkgName:String): List<Command> {
        twoAppList = repo.commandRepo.selectFromPkgName(pkgName).take(6)
        return twoAppList
    }

    fun resetUxValue(){
        distance = repo.preference.getDistance()

        gridCnt = repo.preference.getGridCount()
        gridViewSize = repo.preference.getGridViewSize()
        gridItemSize = gridViewSize.toPixel()/gridCnt
        twoStepOpenInterval.value = repo.preference.getTwoStepOpenInterval()

    }

    fun resetTwoStepValues(){

        for( i in 0 until 4) {
            val tmpApps = repo.preference.getQuickApps(i)
            tmpApps.forEachIndexed { i, quickApp ->
                if(repo.commandRepo.selectFromPkgName(quickApp.commonApp.pkgName).isNotEmpty() &&
                    tmpApps[i].type == QuickAppType.ONE_APP) {
                    tmpApps[i].type = QuickAppType.TWO_APP
                } else if(repo.commandRepo.selectFromPkgName(quickApp.commonApp.pkgName).isEmpty() &&
                    tmpApps[i].type == QuickAppType.TWO_APP) {
                    tmpApps[i].type = QuickAppType.ONE_APP
                }
            }
            repo.preference.setQuickApps(tmpApps,i)
        }

    }

    fun resetBgBitmap(){
        SC.bgBitmap = repo.backgroundRepo.loadBitmap()
        bgBitmap.set(SC.bgBitmap)
    }

    fun resetBgWidgets(){
        repo.preference.run {
            bgwi.set(getBgWidgetInfos())
            useTime.set(getBgTimeUse())
            useAmpm.set(getBgAmpmUse())
            useDate.set(getBgDateUse())
            useDow.set(getBgDowUse())
            useText.set(getBgTextUse())
        }
    }

    fun setDeviceXY(x:Int,y:Int){
        repo.preference.run {
            setDeviceX(x)
            setDeviceY(y)
        }
    }

    fun appInit(){
        val installedApps = getInstalledApps()
        repo.preference.setDrawerApps(installedApps)
    }

    fun setAppsFromDir(dir: Int) {
        lastestDir = dir
        liveDataApps.value = repo.preference.getQuickApps(dir)
    }

    fun clickTwoStepItem(pos:Int) {
        toActMsg(MsgType.PICK_TWO_STEP_ITEM,pos)
    }

    fun getInstalledApps(): MutableList<CommonApp> {

        val packageManager = App.get.packageManager
        val intent = Intent(Intent.ACTION_MAIN, null)
            .apply { addCategory(Intent.CATEGORY_LAUNCHER) }

        return packageManager.queryIntentActivities(intent,MATCH_ALL)
            .map { it.activityInfo }
            .map { CommonApp( it.packageName, it.loadLabel(packageManager).toString(), false ) }
            .toMutableList()
    }

}

enum class Step{
    NONE,
    TOUCH_START,
    OPEN_ONE,
    OPEN_TWO,
}

enum class StepView{
    APP_STARTER,
    ONE_STEP,
    TWO_STEP,
}