package seoft.co.kr.launcherq.ui.main

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import kotlinx.android.synthetic.main.dialog_setting_in_main.*
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.utill.i
import android.view.WindowManager
import android.util.DisplayMetrics
import seoft.co.kr.launcherq.ui.setting.BgScreenSettingActivity
import seoft.co.kr.launcherq.ui.splash.SplashActivity


class SettingInMainDialog(context:Context,val cb:(Any)->Unit ) : Dialog(context) {

    val TAG = "SettingInMainDialog#$#"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_setting_in_main)

        initListener()
    }

    fun initListener(){
        tvBgScreenSetting.setOnClickListener { v ->

            "tvBgScreenSetting".i(TAG)

            cb.invoke(BgScreenSettingActivity::class.java)
            dismiss()
        }


    }





}