package seoft.co.kr.launcherq.ui.drawer

import android.content.Context
import android.graphics.BitmapFactory
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.model.CAppException
import seoft.co.kr.launcherq.data.model.QuickApp
import seoft.co.kr.launcherq.data.model.QuickAppType
import seoft.co.kr.launcherq.utill.App
import seoft.co.kr.launcherq.utill.SC
import java.io.File
import java.io.FileInputStream


class SimpleImageAdapter(val context:Context, val gridInterval:Int, val qApps: MutableList<QuickApp>, val curDir:Int) : BaseAdapter() {

    val TAG = "SimpleImageAdapter#$#"

    override fun getView(pos: Int, view: View?, parent: ViewGroup?): View {
        var iv = ImageView(context)

        view?.let {
            iv = view as ImageView
        }?: let {
            iv.layoutParams = ViewGroup.LayoutParams(gridInterval,gridInterval)
            iv.scaleType = ImageView.ScaleType.FIT_XY
        }

        if(qApps[pos].hasImg) {

            // SC.imgDir is saved in BackgroundRepo class
            iv.setImageBitmap(BitmapFactory.decodeStream(FileInputStream(File(SC.imgDir,"$curDir#$pos"))))
        }
        else if(qApps[pos].commonApp.isExcept) iv.setImageResource( CAppException.values().find { it.get == qApps[pos].commonApp.pkgName }?.rss ?: R.drawable.ic_error_orange )
        else {
            when(qApps[pos].type) {
                QuickAppType.ONE_APP,QuickAppType.TWO_APP -> iv.setImageDrawable(App.get.packageManager.getApplicationIcon(qApps[pos].commonApp.pkgName))
                QuickAppType.FOLDER -> iv.setImageResource(R.drawable.ic_folder_im)
                QuickAppType.EXPERT -> iv.setImageResource(R.drawable.ic_expert_im)
                else -> {}
            }
        }

        return iv
    }

    override fun getItem(pos: Int) = null
    override fun getItemId(p0: Int): Long = 0
    override fun getCount(): Int = qApps.count()


}