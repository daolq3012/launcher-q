package seoft.co.kr.launcherq.ui.main

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import android.graphics.Bitmap
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.data.model.BackgroundWidgetInfos
import seoft.co.kr.launcherq.data.model.CommonApp
import seoft.co.kr.launcherq.data.model.QuickApp
import seoft.co.kr.launcherq.data.model.QuickAppType
import seoft.co.kr.launcherq.ui.MsgType
import seoft.co.kr.launcherq.ui.ViewModelHelper
import seoft.co.kr.launcherq.utill.InstalledAppUtil
import seoft.co.kr.launcherq.utill.toPixel

class MainViewModel(val repo: Repo): ViewModelHelper() {

    val TAG = "MainViewModel#$#"

    val bgBitmap : ObservableField<Bitmap> by lazy { ObservableField(repo.backgroundRepo.loadBitmap()) }
    val bgwi : ObservableField<BackgroundWidgetInfos> by lazy { ObservableField(repo.preference.getBgWidgetInfos()) }

    val useTime : ObservableField<Boolean> by lazy { ObservableField(repo.preference.getBgTimeUse()) }
    val useAmpm : ObservableField<Boolean> by lazy { ObservableField(repo.preference.getBgAmpmUse()) }
    val useDate : ObservableField<Boolean> by lazy { ObservableField(repo.preference.getBgDateUse()) }
    val useDow : ObservableField<Boolean> by lazy { ObservableField(repo.preference.getBgDowUse()) }
    val useText : ObservableField<Boolean> by lazy { ObservableField(repo.preference.getBgTextUse()) }

    var liveDataApps = MutableLiveData<MutableList<QuickApp>>()

    val EMPTY_QUICK_APP = QuickApp(CommonApp("","","",false),QuickAppType.EMPTY)
    val twoStepApp = ObservableField<QuickApp>(EMPTY_QUICK_APP)

    val step = ObservableField<Step>(Step.NONE)
    var lastestDir = 0
    var gridItemSize = 0

    // loading property
    var gridCnt = 0
    var gridViewSize = 0
    var distance = 0 // = square of 1/2
    var twoStepOpenInterval = 0 // 1 is 200ms

    override fun start() {

        repo.preference.run {
            if(isFirstLaunch()) {
                appInit()
                setIsFirst(false)
            }
        }

        resetUxValue()

    }

    fun emptyTwoStepApp(){ twoStepApp.set(EMPTY_QUICK_APP) }

    fun resetUxValue(){
        distance = repo.preference.getDistance()

        gridCnt = repo.preference.getGridCount()
        gridViewSize = repo.preference.getGridViewSize()
        gridItemSize = gridViewSize.toPixel()/gridCnt
        twoStepOpenInterval = repo.preference.getTwoStepOpenInterval()
    }

    fun resetBgBitmap(){ bgBitmap.set(repo.backgroundRepo.loadBitmap()) }

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
        val installedApps = InstalledAppUtil().getInstalledApps()
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