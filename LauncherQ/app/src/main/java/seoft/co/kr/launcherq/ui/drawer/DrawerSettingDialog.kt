package seoft.co.kr.launcherq.ui.drawer

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.view.Window
import kotlinx.android.synthetic.main.dialog_setting_drawer.*
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.utill.SC


class DrawerSettingDialog(context: Context, val repo: Repo,var cb: (boolean: Boolean)->Unit) : Dialog(context) {

    val TAG = "DrawerSettingDialog#$#"

    var curX : Int = 0
    var curY : Int = 0

    val MIN_CUR_X = 5
    val MAX_CUR_X = 8

    val MIN_CUR_Y = 5
    val MAX_CUR_Y = 9


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_setting_drawer)


        curX = repo.preference.getDrawerColumnNum()
        curY = repo.preference.getDrawerItemNum() / curX

        initViewAndListener()

    }

    fun initViewAndListener(){

        tvNumX.text = curX.toString()
        tvNumY.text = curY.toString()

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
            context.startActivity(Intent(context,DrawerHideActivity::class.java))
        }

        tvOk.setOnClickListener { v ->
            repo.preference.setDrawerColumnNum(curX)
            repo.preference.setDrawerItemNum(curX * curY)
            cb.invoke(true)
            dismiss()
        }

        tvCancel.setOnClickListener { v ->
            cb.invoke(false)
            dismiss()
        }

    }

}