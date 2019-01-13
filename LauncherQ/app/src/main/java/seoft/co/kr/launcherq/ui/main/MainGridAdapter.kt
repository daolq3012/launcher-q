package seoft.co.kr.launcherq.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.item_main_app.view.*
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.model.QuickApp
import seoft.co.kr.launcherq.data.model.QuickAppType
import seoft.co.kr.launcherq.utill.App

class MainGridAdapter(val context: Context, val qApps: MutableList<QuickApp>, val cb:(QuickApp)->Unit) : BaseAdapter() {

    override fun getView(pos: Int, view: View?, parent: ViewGroup?): View {

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val item = inflater.inflate(R.layout.item_main_app,null)

        when(qApps[pos].type) {
            QuickAppType.EMPTY -> item.ivIcon.setImageResource(R.drawable.ic_close_white)
            QuickAppType.FOLDER -> item.ivIcon.setImageResource(R.drawable.ic_folder_green)
            QuickAppType.ONE_APP -> item.ivIcon.setImageDrawable(App.get.packageManager.getApplicationIcon(qApps[pos].commonApp.pkgName))
        }

        item.rlMainApp.setOnClickListener { _ -> cb.invoke(qApps[pos]) }

        return item
    }

    override fun getItem(pos: Int) = null
    override fun getItemId(p0: Int): Long = 0
    override fun getCount(): Int = qApps.count()

}