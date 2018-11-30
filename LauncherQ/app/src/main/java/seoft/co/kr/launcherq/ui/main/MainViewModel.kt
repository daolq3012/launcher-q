package seoft.co.kr.launcherq.ui.main

import android.databinding.ObservableField
import android.graphics.Bitmap
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.data.model.BackgroundWidgetInfos
import seoft.co.kr.launcherq.ui.ViewModelHelper
import seoft.co.kr.launcherq.utill.InstalledAppUtil

class MainViewModel(val repo: Repo): ViewModelHelper() {

    val TAG = "MainViewModel#$#"

    val bgBitmap : ObservableField<Bitmap> by lazy { ObservableField(repo.backgroundRepo.loadBitmap()) }
    val bgwi : ObservableField<BackgroundWidgetInfos> by lazy { ObservableField(repo.preference.getBgWidgetInfos()) }

    val useTime : ObservableField<Boolean> by lazy { ObservableField(repo.preference.getBgTimeUse()) }
    val useAmpm : ObservableField<Boolean> by lazy { ObservableField(repo.preference.getBgAmpmUse()) }
    val useDate : ObservableField<Boolean> by lazy { ObservableField(repo.preference.getBgDateUse()) }
    val useDow : ObservableField<Boolean> by lazy { ObservableField(repo.preference.getBgDowUse()) }
    val useText : ObservableField<Boolean> by lazy { ObservableField(repo.preference.getBgTextUse()) }

    override fun start() {

        repo.preference.run {
            if(isFirstLaunch()) {
                appInit()

                setIsFirst(false)
            }
        }
    }

    fun resetBgBitmap(){
        bgBitmap.set(repo.backgroundRepo.loadBitmap())
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
        val installedApps = InstalledAppUtil().getInstalledApps()
        repo.preference.setDrawerApps(installedApps)

    }



}