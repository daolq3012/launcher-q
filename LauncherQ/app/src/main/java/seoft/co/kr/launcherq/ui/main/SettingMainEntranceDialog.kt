package seoft.co.kr.launcherq.ui.main

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import kotlinx.android.synthetic.main.dialog_setting_main_entrance.*
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.ui.arrange.ArrangeActivity
import seoft.co.kr.launcherq.ui.setting.BgScreenSettingActivity
import seoft.co.kr.launcherq.ui.setting.LauncherSettingActivity


class SettingMainEntranceDialog(context:Context, val cb:(Any)->Unit ) : Dialog(context) {

    val TAG = "SettingMainEntranceDialog#$#"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_setting_main_entrance)

        initListener()
    }

    fun initListener(){
        tvBgScreenSetting.setOnClickListener { v ->
            context.startActivity(Intent(context,BgScreenSettingActivity::class.java))
            dismiss()
        }

        // TODO need to remove : for 앱 세팅바로가기
        tvBgEtcSetting.setOnClickListener { v ->
            context.startActivity(Intent(context,ArrangeActivity::class.java))
            dismiss()
        }

        tvLauncherSetting.setOnClickListener { v ->
            context.startActivity(Intent(context, LauncherSettingActivity::class.java))
            dismiss()
        }
    }

}