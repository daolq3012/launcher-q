package seoft.co.kr.launcherq.ui.main

import android.databinding.ObservableField
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.ui.MsgType
import seoft.co.kr.launcherq.ui.ViewModelHelper
import seoft.co.kr.launcherq.ui.splash.SplashActivity
import seoft.co.kr.launcherq.utill.App

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