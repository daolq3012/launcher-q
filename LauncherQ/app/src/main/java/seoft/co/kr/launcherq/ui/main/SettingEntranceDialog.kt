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
import seoft.co.kr.launcherq.utill.SelectorDialog


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
//            cb.invoke(MsgType.START_BG_SETTING_ACTIVITY)
            context.startActivity(Intent(context,BgScreenSettingActivity::class.java))
            dismiss()
        }

//        tvBgEtcSetting.setOnClickListener { v ->
//            val sd = SelectorDialog(
//                context,
//                "위젯 설정",
//                SelectorDialog.DialogSelectorInfo("시간 위젯",R.color.gray),
//                SelectorDialog.DialogSelectorInfo("날짜 위젯",R.color.gray),
//                SelectorDialog.DialogSelectorInfo("요일 위젯",R.color.gray),
//                SelectorDialog.DialogSelectorInfo("글귀 위젯",R.color.gray)
//            ) {
//                it.toString().i(TAG)
//            }
//            sd.create()
//            dismiss()
//        }


    }





}