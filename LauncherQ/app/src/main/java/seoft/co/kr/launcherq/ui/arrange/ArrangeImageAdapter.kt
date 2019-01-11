package seoft.co.kr.launcherq.ui.arrange

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.item_arrange_app.view.*
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.model.QuickApp
import seoft.co.kr.launcherq.utill.App


class ArrangeImageAdapter(val context:Context, val qApps: MutableList<QuickApp>, val cb:(CallbackArrangeGrid)->Unit) : BaseAdapter() {



    override fun getView(pos: Int, view: View?, parent: ViewGroup?): View {


        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val item = inflater.inflate(R.layout.item_arrange_app,null)

        if(qApps[pos].commonApp.pkgName.isEmpty())
            item.ivIcon.setImageResource(R.drawable.ic_close_white)
        else
            item.ivIcon.setImageDrawable(App.get.packageManager.getApplicationIcon(qApps[pos].commonApp.pkgName))

        item.rlArrangeApp.setOnClickListener { _ ->
            qApps.forEach { it.commonApp.isHide = false }
            qApps[pos].commonApp.isHide = true
            notifyDataSetChanged()
            cb.invoke(CallbackArrangeGrid(qApps[pos],pos))
        }


        if(qApps[pos].commonApp.isHide)
            item.ivSelect.visibility = View.VISIBLE
        else
            item.ivSelect.visibility = View.INVISIBLE

        return item
    }

    override fun getItem(pos: Int) = null
    override fun getItemId(p0: Int): Long = 0
    override fun getCount(): Int = qApps.count()

    data class CallbackArrangeGrid(var quickApp: QuickApp, var pos:Int)

}