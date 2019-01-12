package seoft.co.kr.launcherq.ui.arrange

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.model.CommonApp
import seoft.co.kr.launcherq.data.model.toCommonApp
import seoft.co.kr.launcherq.utill.App

class ArrangeFolderAdapter(context: Context, val appList: List<String>, val cb:(CommonApp)->Unit ) : BaseAdapter(){
    override fun getView(pos: Int, convertView: View?, parant: ViewGroup?): View {
        val view = inflater.inflate(R.layout.item_arrange_folder,parant,false)

        val cApp = appList[pos].toCommonApp()

        val ivIcon = view.findViewById<ImageView>(R.id.ivIcon)
        val tvName = view.findViewById<TextView>(R.id.tvName)
        val llFolderApp = view.findViewById<LinearLayout>(R.id.llFolderApp)

        val icon = App.get.packageManager.getApplicationIcon(cApp.pkgName)
        ivIcon.setImageDrawable(icon)
        tvName.text = cApp.label

        llFolderApp.setOnClickListener { v -> cb.invoke(cApp) }


        return view
    }

    override fun getItem(pos: Int): Any = appList[pos]

    override fun getItemId(pos: Int): Long  = pos.toLong()

    override fun getCount(): Int = appList.size

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

}