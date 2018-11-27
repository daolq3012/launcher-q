package seoft.co.kr.launcherq.ui.main

import android.databinding.ObservableField
import android.graphics.Bitmap
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.ui.ViewModelHelper

class MainViewModel(val repo: Repo): ViewModelHelper() {

    val TAG = "MainViewModel#$#"

    val bgBitmap : ObservableField<Bitmap> by lazy { ObservableField(repo.backgroundRepo.loadBitmap()) }

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

    fun setDeviceXY(x:Int,y:Int){
        repo.preference.run {
            setDeviceX(x)
            setDeviceY(y)
        }
    }

    fun appInit(){



    }



}