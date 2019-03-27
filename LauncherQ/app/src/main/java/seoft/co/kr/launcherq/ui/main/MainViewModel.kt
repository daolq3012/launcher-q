package seoft.co.kr.launcherq.ui.main

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.data.model.BackgroundWidgetInfos
import seoft.co.kr.launcherq.data.model.Command
import seoft.co.kr.launcherq.data.model.QuickApp
import seoft.co.kr.launcherq.data.model.QuickAppType
import seoft.co.kr.launcherq.ui.MsgType
import seoft.co.kr.launcherq.ui.ViewModelHelper
import seoft.co.kr.launcherq.utill.App
import seoft.co.kr.launcherq.utill.InstantUtil
import seoft.co.kr.launcherq.utill.SC
import seoft.co.kr.launcherq.utill.toPixel
import java.io.File
import java.io.FileInputStream

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
        initDrawerAndImageCache()
    }

    fun initDrawerAndImageCache(){

        SC.drawerApps = repo.preference.getDrawerApps()

        SC.drawerApps
            .forEach {
                try {
                    repo.imageCacheRepo.saveCache(it.pkgName,App.get.packageManager.getApplicationIcon(it.pkgName))
                } catch (e:Exception){
                    val tmpPkgName = it.pkgName
                    SC.drawerApps = SC.drawerApps.
                        asSequence()
                        .filterNot { it.pkgName == tmpPkgName }
                        .toMutableList()
                    repo.preference.setDrawerApps(SC.drawerApps)
                }
            }

        // set custom icon images
        for(dir in 0 until 4) {
            val qApps = Repo.preference.getQuickApps(dir)
            for ( pos in 0 until 16) {
                val curDir = "${dir}#${pos}"
                if(qApps[pos].hasImg) {
                    val bitmap = BitmapFactory.decodeStream(FileInputStream(File(SC.imgDir,curDir)))
                    repo.imageCacheRepo.saveCache(curDir,BitmapDrawable(App.get.resources, bitmap))
                }
            }
        }


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

        SC.FLING_BOTTOM_BOUNDARY = repo.preference.getBottomBoundary()
        SC.FLING_TOP_BOUNDARY = repo.preference.getTopBoundary()

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
        val installedApps = InstantUtil().getInstalledApps()
        repo.preference.setDrawerApps(installedApps)
    }

    fun setAppsFromDir(dir: Int) {
        lastestDir = dir
        liveDataApps.value = repo.preference.getQuickApps(dir)
    }

    fun clickTwoStepItem(pos:Int) {
        toActMsg(MsgType.PICK_TWO_STEP_ITEM,pos)
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