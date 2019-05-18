package seoft.co.kr.launcherq.ui.main

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Window
import kotlinx.android.synthetic.main.dialog_setting_main_entrance.*
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.ui.arrange.ArrangeActivity
import seoft.co.kr.launcherq.ui.setting.BgScreenSettingActivity
import seoft.co.kr.launcherq.ui.setting.LauncherSettingActivity
import seoft.co.kr.launcherq.utill.App
import seoft.co.kr.launcherq.utill.TRANS
import seoft.co.kr.launcherq.utill.toast


class SettingMainEntranceDialog(context:Context, val cb:(Any)->Unit ) : Dialog(context) {

    val TAG = "SettingMainEntranceDialog#$#"

    val LAQ_STORE_PKG_NAME = "seoft.co.kr.laq_store"

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


        tvThemeSetting.setOnClickListener { v ->

            try {
                App.get.startActivity(context.packageManager.getLaunchIntentForPackage(LAQ_STORE_PKG_NAME))
            } catch (e : Exception) {
                R.string.need_to_laqstore.TRANS().toast()
                App.get.startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$LAQ_STORE_PKG_NAME"))
                )
            }

        }
    }

}