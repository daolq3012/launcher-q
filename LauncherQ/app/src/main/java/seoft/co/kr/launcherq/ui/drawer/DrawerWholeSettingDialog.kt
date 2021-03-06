package seoft.co.kr.launcherq.ui.drawer

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import kotlinx.android.synthetic.main.dialog_setting_drawer_whole.*
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.utill.TRANS


class DrawerWholeSettingDialog(context: Context, val drawerMode: DrawerViewModel.DrawerMode, val repo: Repo, var cb: (dsdr:DrawerSettingDialogResult)->Unit) : Dialog(context) {

    val TAG = "DrawerWholeSettingDialog#$#"

    var curX : Int = 0
    var curY : Int = 0

    val MIN_CUR_X = 5
    val MAX_CUR_X = 8

    val MIN_CUR_Y = 5
    val MAX_CUR_Y = 9


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_setting_drawer_whole)


        curX = repo.preference.getDrawerColumnNum()
        curY = repo.preference.getDrawerItemNum() / curX

        initViewAndListener()

    }

    fun initViewAndListener(){

        tvNumX.text = curX.toString()
        tvNumY.text = curY.toString()

        tvHide.text = if(DrawerViewModel.DrawerMode.HIDE_MODE == drawerMode) R.string.default_app_list.TRANS() else R.string.hide_app_list.TRANS()


        tvNumX.setOnClickListener { v ->
            if(curX >= MAX_CUR_X) curX = MIN_CUR_X
            else curX++

            tvNumX.text = curX.toString()
        }

        tvNumY.setOnClickListener { v ->
            if(curY >= MAX_CUR_Y) curY = MIN_CUR_Y
            else curY++

            tvNumY.text = curY.toString()
        }

        tvHide.setOnClickListener { v ->
            cb.invoke(DrawerSettingDialogResult.SET_HIDE_APP)
            dismiss()
        }

        tvOk.setOnClickListener { v ->
            repo.preference.setDrawerColumnNum(curX)
            repo.preference.setDrawerItemNum(curX * curY)
            cb.invoke(DrawerSettingDialogResult.OK)
            dismiss()
        }

        tvCancel.setOnClickListener { v ->
            cb.invoke(DrawerSettingDialogResult.CANCEL)
            dismiss()
        }

    }

    enum class DrawerSettingDialogResult{
        OK,
        CANCEL,
        SET_HIDE_APP
    }

}