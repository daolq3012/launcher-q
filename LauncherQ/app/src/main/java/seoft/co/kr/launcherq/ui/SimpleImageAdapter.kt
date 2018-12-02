package seoft.co.kr.launcherq.ui

import android.content.Context
import android.media.Image
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import seoft.co.kr.launcherq.data.model.CommonApp
import seoft.co.kr.launcherq.utill.App
import kotlin.contracts.contract
import android.util.TypedValue
import android.util.DisplayMetrics



class SimpleImageAdapter(val context:Context, val gridInterval:Int, val cApps: MutableList<CommonApp>) : BaseAdapter() {


    override fun getView(pos: Int, view: View?, parent: ViewGroup?): View {
        var iv = ImageView(context)

        view?.let {
            iv = view as ImageView
        }?: let {
            val dm = context.resources.displayMetrics

            iv.layoutParams = ViewGroup.LayoutParams(toPixels(gridInterval,dm),toPixels(gridInterval,dm))
            iv.scaleType = ImageView.ScaleType.FIT_XY
        }

        iv.setImageDrawable(App.get.packageManager.getApplicationIcon(cApps[pos].pkgName))

        return iv
    }

    override fun getItem(pos: Int) = null
    override fun getItemId(p0: Int): Long = 0
    override fun getCount(): Int = cApps.count()

    fun toPixels(dp: Int, metrics: DisplayMetrics): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), metrics).toInt()
    }
//
//픽셀맞추기중
//    그리드뷰 뜨는거 픽셀맞추고
//    저장세이브 계쏙진행


}