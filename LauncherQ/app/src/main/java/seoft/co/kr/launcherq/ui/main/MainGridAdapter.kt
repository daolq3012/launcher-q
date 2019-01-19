package seoft.co.kr.launcherq.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.item_main_app.view.*
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.model.CAppException
import seoft.co.kr.launcherq.data.model.QuickApp
import seoft.co.kr.launcherq.data.model.QuickAppType
import seoft.co.kr.launcherq.utill.App

/**
 * need to insert pixel value into gridItemSize param
 */
class MainGridAdapter(val context: Context, val qApps: MutableList<QuickApp>, val gridItemSize:Int, val cb:(CallbackMainGrid)->Unit) : BaseAdapter() {

    override fun getView(pos: Int, view: View?, parent: ViewGroup?): View {

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val item = inflater.inflate(R.layout.item_main_app,null)

        if(qApps[pos].commonApp.isExcept) {

            when(qApps[pos].commonApp.pkgName) {
                CAppException.DRAWER.get -> item.ivIcon.setImageResource(R.drawable.ic_widgets_orange)
            }

        } else {
            when(qApps[pos].type) {
                QuickAppType.EMPTY -> item.ivIcon.setImageResource(R.drawable.ic_close_white)
                QuickAppType.FOLDER -> item.ivIcon.setImageResource(R.drawable.ic_folder_green)
                QuickAppType.ONE_APP -> item.ivIcon.setImageDrawable(App.get.packageManager.getApplicationIcon(qApps[pos].commonApp.pkgName))
            }
        }


        val params = RelativeLayout.LayoutParams(gridItemSize, gridItemSize)
        item.ivEtc.layoutParams = params
        item.ivIcon.layoutParams = params

        item.rlMainApp.setOnClickListener { _ -> cb.invoke(CallbackMainGrid(qApps[pos],false)) }
        item.rlMainApp.setOnLongClickListener { _ -> cb.invoke(CallbackMainGrid(qApps[pos],true))
        true
        }

        return item
    }

    override fun getItem(pos: Int) = null
    override fun getItemId(p0: Int): Long = 0
    override fun getCount(): Int = qApps.count()

}

data class CallbackMainGrid(var quickApp: QuickApp, var isLongClick:Boolean)
