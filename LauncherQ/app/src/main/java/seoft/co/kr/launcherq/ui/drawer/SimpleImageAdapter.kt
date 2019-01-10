package seoft.co.kr.launcherq.ui.drawer

import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import seoft.co.kr.launcherq.data.model.CommonApp
import seoft.co.kr.launcherq.utill.App


class SimpleImageAdapter(val context:Context, val gridInterval:Int, val cApps: MutableList<CommonApp>) : BaseAdapter() {

    val TAG = "SimpleImageAdapter#$#"

    override fun getView(pos: Int, view: View?, parent: ViewGroup?): View {
        var iv = ImageView(context)

        view?.let {
            iv = view as ImageView
        }?: let {
            val dm = context.resources.displayMetrics

            iv.layoutParams = ViewGroup.LayoutParams(toPixels(gridInterval,dm),toPixels(gridInterval,dm))
            iv.scaleType = ImageView.ScaleType.FIT_XY
        }

//        "cApps[pos].pkgName ${cApps[pos].pkgName.toString()}".i(TAG)
        if(!cApps[pos].pkgName.isEmpty())
            iv.setImageDrawable(App.get.packageManager.getApplicationIcon(cApps[pos].pkgName))

        return iv
    }

    override fun getItem(pos: Int) = null
    override fun getItemId(p0: Int): Long = 0
    override fun getCount(): Int = cApps.count()

    fun toPixels(dp: Int, metrics: DisplayMetrics): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), metrics).toInt()
    }
// TODO
// 픽셀맞추기중
// 그리드뷰 뜨는거 픽셀맞추고
// 저장세이브 계쏙진행
// 혹 잘되면 QuickImageAdapter도 적용


}