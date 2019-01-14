package seoft.co.kr.launcherq.ui.drawer

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.model.QuickApp
import seoft.co.kr.launcherq.data.model.QuickAppType
import seoft.co.kr.launcherq.utill.App


class SimpleImageAdapter(val context:Context, val gridInterval:Int, val qApps: MutableList<QuickApp>) : BaseAdapter() {

    val TAG = "SimpleImageAdapter#$#"

    override fun getView(pos: Int, view: View?, parent: ViewGroup?): View {
        var iv = ImageView(context)

        view?.let {
            iv = view as ImageView
        }?: let {
            iv.layoutParams = ViewGroup.LayoutParams(gridInterval,gridInterval)
            iv.scaleType = ImageView.ScaleType.FIT_XY
        }

//        "cApps[pos].pkgName ${cApps[pos].pkgName.toString()}".i(TAG)
        if(qApps[pos].type == QuickAppType.ONE_APP || qApps[pos].type == QuickAppType.TWO_APP)
            iv.setImageDrawable(App.get.packageManager.getApplicationIcon(qApps[pos].commonApp.pkgName))
        else if(qApps[pos].type == QuickAppType.FOLDER)
            iv.setImageResource(R.drawable.ic_folder_green)

        return iv
    }

    override fun getItem(pos: Int) = null
    override fun getItemId(p0: Int): Long = 0
    override fun getCount(): Int = qApps.count()


}