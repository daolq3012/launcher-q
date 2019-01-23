package seoft.co.kr.launcherq.ui.arrange

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.item_arrange_app.view.*
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.model.CAppException
import seoft.co.kr.launcherq.data.model.QuickApp
import seoft.co.kr.launcherq.data.model.QuickAppType
import seoft.co.kr.launcherq.utill.App


/**
 * need to insert pixel value into itemSize param
 */
class ArrangeImageAdapter(val context:Context, val qApps: MutableList<QuickApp>, val itemSize:Int, val cb:(CallbackArrangeGrid)->Unit) : BaseAdapter() {

    override fun getView(pos: Int, view: View?, parent: ViewGroup?): View {

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val item = inflater.inflate(R.layout.item_arrange_app,null)

        if(qApps[pos].commonApp.isExcept) {

            item.ivIcon.setImageResource(
                CAppException.values().find { it.get == qApps[pos].commonApp.pkgName }?.rss ?: R.drawable.ic_error_orange
            )

        } else {
            when (qApps[pos].type) {
                QuickAppType.EMPTY -> item.ivIcon.setImageResource(R.drawable.ic_close_white)
                QuickAppType.FOLDER -> item.ivIcon.setImageResource(R.drawable.ic_folder_green)
                QuickAppType.ONE_APP -> item.ivIcon.setImageDrawable(App.get.packageManager.getApplicationIcon(qApps[pos].commonApp.pkgName))
                QuickAppType.EXPERT -> item.ivIcon.setImageResource(R.drawable.ic_build_orange)
            }
        }

        item.rlArrangeApp.setOnClickListener { _ ->
            qApps.forEach { it.isPicked = false }
            qApps[pos].isPicked = true
            notifyDataSetChanged()
            cb.invoke(CallbackArrangeGrid(qApps[pos],pos))
        }

        if(qApps[pos].isPicked) item.ivSelect.visibility = View.VISIBLE
        else item.ivSelect.visibility = View.INVISIBLE

        val params = RelativeLayout.LayoutParams(itemSize,itemSize)
        item.ivSelect.layoutParams = params
        item.ivIcon.layoutParams = params

        return item
    }

    override fun getItem(pos: Int) = null
    override fun getItemId(p0: Int): Long = 0
    override fun getCount(): Int = qApps.count()

    data class CallbackArrangeGrid(var quickApp: QuickApp, var pos:Int)

}