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
import seoft.co.kr.launcherq.data.model.QuickAppType


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

            val apps = repo.preference.getQuickApps(i)//.map { it.commonApp }

            if(apps.all { it.type == QuickAppType.EMPTY}){
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

    fun setDir(drawerAppSettingType : DrawerAppSettingType) : Boolean {
        cb.invoke(drawerAppSettingType)
        dismiss()
        return true
    }

    fun initListener(){

        llSetTop.setOnClickListener { _ -> setDir(DrawerAppSettingType.SET_TOP) }
        gvTop.setOnTouchListener { _, _ -> setDir(DrawerAppSettingType.SET_TOP) }

        llSetRight.setOnClickListener { _ -> setDir(DrawerAppSettingType.SET_RIGHT) }
        gvRight.setOnTouchListener { _, _ -> setDir(DrawerAppSettingType.SET_RIGHT)  }

        llSetLeft.setOnClickListener { _ -> setDir(DrawerAppSettingType.SET_LEFT) }
        gvLeft.setOnTouchListener { _, _ -> setDir(DrawerAppSettingType.SET_LEFT)  }

        llSetBottom.setOnClickListener { _ -> setDir(DrawerAppSettingType.SET_BOTTOM) }
        gvBottom.setOnTouchListener { _, _ -> setDir(DrawerAppSettingType.SET_BOTTOM)  }

        llSetMid.setOnClickListener { _ -> setDir(DrawerAppSettingType.SET_TOP) }


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

