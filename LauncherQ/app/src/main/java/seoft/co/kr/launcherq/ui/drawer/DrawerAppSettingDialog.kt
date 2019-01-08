package seoft.co.kr.launcherq.ui.drawer

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.GridView
import android.widget.TextView
import kotlinx.android.synthetic.main.dialog_setting_drawer_app.*
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.Repo


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

            val apps = repo.preference.getQuickApps(i).map { it.commonApp }

            if(apps.all { it.pkgName == "" }){
                grids[i].visibility = View.GONE
            } else {
                grids[i].numColumns = gridCnt
                grids[i].adapter = SimpleImageAdapter(
                    context,
                    78 / gridCnt,
                    apps.take(gridCnt * gridCnt).toMutableList()
                ) // 78 is @dimen/grid_size_in_app_setting_view - 2
            }
        }

    }

    fun initListener(){

        llSetTop.setOnClickListener { _ ->
            cb.invoke(DrawerAppSettingType.SET_TOP)
            dismiss()
        }

        llSetRight.setOnClickListener { _ ->
            cb.invoke(DrawerAppSettingType.SET_RIGHT)
            dismiss()
        }

        llSetBottom.setOnClickListener { _ ->
            cb.invoke(DrawerAppSettingType.SET_BOTTOM)
            dismiss()
        }

        llSetLeft.setOnClickListener { _ ->
            cb.invoke(DrawerAppSettingType.SET_LEFT)
            dismiss()
        }

        llSetMid.setOnClickListener { _ ->
            cb.invoke(DrawerAppSettingType.SET_TOP)
            dismiss()
        }


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
        SET_HIDE,
        SET_TOP,
        SET_LEFT,
        SET_RIGHT,
        SET_BOTTOM,
    }

}