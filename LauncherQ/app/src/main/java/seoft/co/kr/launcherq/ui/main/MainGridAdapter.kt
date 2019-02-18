package seoft.co.kr.launcherq.ui.main

import android.content.Context
import android.graphics.BitmapFactory
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
import seoft.co.kr.launcherq.utill.SC
import java.io.File
import java.io.FileInputStream

/**
 * need to insert pixel value into gridItemSize param
 */
class MainGridAdapter(val context: Context, val qApps: MutableList<QuickApp>, val gridItemSize:Int, val curDir:Int, val cb:(CallbackMainGrid)->Unit) : BaseAdapter() {

    override fun getView(pos: Int, view: View?, parent: ViewGroup?): View {

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val item = inflater.inflate(R.layout.item_main_app,null)

        if(qApps[pos].hasImg) {

            // SC.imgDir is saved in BackgroundRepo class
            val f = File(SC.imgDir,"$curDir#$pos")
            val b = BitmapFactory.decodeStream(FileInputStream(f))
            item.ivIcon.setImageBitmap(b)
        } else if(qApps[pos].commonApp.isExcept) {

            item.ivIcon.setImageResource(
                CAppException.values().find { it.get == qApps[pos].commonApp.pkgName }?.rss ?: R.drawable.ic_error_orange
            )
        } else {
            when(qApps[pos].type) {
                QuickAppType.EMPTY -> item.ivIcon.setImageResource(R.drawable.ic_close_white)
                QuickAppType.FOLDER -> item.ivIcon.setImageResource(R.drawable.ic_folder_green)
                QuickAppType.ONE_APP, QuickAppType.TWO_APP -> item.ivIcon.setImageDrawable(App.get.packageManager.getApplicationIcon(qApps[pos].commonApp.pkgName))
                QuickAppType.EXPERT -> item.ivIcon.setImageResource(R.drawable.ic_build_orange)
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
