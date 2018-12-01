package seoft.co.kr.launcherq.ui.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.model.CommonApp
import seoft.co.kr.launcherq.utill.App
import seoft.co.kr.launcherq.utill.SC

class DrawerAppAdapter(apps_ : List<CommonApp>,page:Int ,itemGridNum:Int ,var cb: (cApp:CommonApp)->Unit) : RecyclerView.Adapter<DrawerAppAdapter.ViewHolder>(){

    var apps = ArrayList<CommonApp>()

    init {
        var stt = page * itemGridNum
        val end = stt + itemGridNum
        while((stt < apps_.size) && (stt < end) )
            apps.add(apps_[stt++])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrawerAppAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_app,parent,false)
        return ViewHolder(view, cb)
    }

    override fun getItemCount() = apps.size

    override fun onBindViewHolder(holder: DrawerAppAdapter.ViewHolder, pos: Int) {
        apps[pos].let{
            val icon = App.get.packageManager.getApplicationIcon(it.pkgName)
            holder.apply {
                ivIcon.setImageDrawable(icon)
                tvName.text = it.label
                cApp = it
            }
        }
    }

    inner class ViewHolder(v: View, var cb: (cApp:CommonApp)->Unit) : RecyclerView.ViewHolder(v) {

        var ivIcon : ImageView
        var tvName : TextView
        lateinit var cApp : CommonApp

        init {
            ivIcon = v.findViewById(R.id.ivIcon)
            tvName = v.findViewById(R.id.tvName)

            ivIcon.setOnClickListener { v ->
                cb.invoke(cApp)
            }
        }

    }

}
