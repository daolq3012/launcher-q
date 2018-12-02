package seoft.co.kr.launcherq.ui.main

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import kotlinx.android.synthetic.main.dialog_setting_entrance.*
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.utill.i
import seoft.co.kr.launcherq.ui.setting.BgScreenSettingActivity


class SettingEntranceDialog(context:Context, val cb:(Any)->Unit ) : Dialog(context) {

    val TAG = "SettingEntranceDialog#$#"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_setting_entrance)

        initListener()
    }

    fun initListener(){
        tvBgScreenSetting.setOnClickListener { v ->
            context.startActivity(Intent(context,BgScreenSettingActivity::class.java))
            dismiss()
        }
    }

}