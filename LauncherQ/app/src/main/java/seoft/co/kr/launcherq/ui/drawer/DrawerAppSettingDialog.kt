package seoft.co.kr.launcherq.ui.drawer

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.GridView
import android.widget.TextView
import kotlinx.android.synthetic.main.dialog_setting_drawer_app.*
import kotlinx.android.synthetic.main.dialog_setting_main_entrance.*
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.data.model.CommonApp
import seoft.co.kr.launcherq.ui.SimpleImageAdapter
import seoft.co.kr.launcherq.ui.setting.BgScreenSettingActivity
import seoft.co.kr.launcherq.utill.i


class DrawerAppSettingDialog(context:Context, val repo: Repo, val cb:(DrawerAppSettingType)->Unit ) : Dialog(context) {

    val TAG = "DrawerAppSettingDialog#$#"


    val grids = mutableListOf<GridView>()
    val emptyTextViews = mutableListOf<TextView>()
//    val cApps = mutableListOf<MutableList<CommonApp>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_setting_drawer_app)

        initView()
        initListener()
    }



    fun initView(){

        grids.add(gvTop)
        grids.add(gvRight)
        grids.add(gvBottom)
        grids.add(gvLeft)

        emptyTextViews.add(tvTop)
        emptyTextViews.add(tvRight)
        emptyTextViews.add(tvBottom)
        emptyTextViews.add(tvLeft)

        val gridCnt = repo.preference.getGridCount()

        for(i in 0 until 4){

            val apps = repo.preference.getQuickApps(i)

            if(apps.all { it.pkgName == "" }){
                grids[i].visibility = View.GONE
            } else {
                grids[i].numColumns = gridCnt
                grids[i].adapter = SimpleImageAdapter(context, 78/gridCnt ,apps.take(gridCnt*gridCnt).toMutableList())
            }
        }

    }

    fun initListener(){

        tvOrder.setOnClickListener { v ->
            cb.invoke(DrawerAppSettingType.SET_ORDER)
            dismiss()
        }

        tvHide.setOnClickListener { v ->
            cb.invoke(DrawerAppSettingType.SET_HIDE)
            dismiss()
        }
    }

    enum class DrawerAppSettingType{
        SET_ORDER,
        SET_HIDE
    }

}