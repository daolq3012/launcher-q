package seoft.co.kr.launcherq.ui.main

import android.databinding.ObservableField
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

    val ofResetBg = ObservableField(false)

    override fun start() {

        repo.preference.run {
            if(isFirstLaunch()) {
                appInit()
                setIsFirst(false)
            }
        }

        ofResetBg.set(true)

    }

    fun appInit(){

        val bitImg = BitmapFactory.decodeResource(App.get.resources, R.drawable.default_bg)
        repo.backgroundRepo.saveBitmap(bitImg,App.get.applicationContext)





    }


}