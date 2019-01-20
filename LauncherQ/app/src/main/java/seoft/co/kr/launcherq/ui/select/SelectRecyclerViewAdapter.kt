package seoft.co.kr.launcherq.ui.select

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import seoft.co.kr.launcherq.data.model.CAppException
import seoft.co.kr.launcherq.data.model.CommonApp
import seoft.co.kr.launcherq.databinding.ItemSelectBinding
import seoft.co.kr.launcherq.utill.App

class SelectRecyclerViewAdapter(var cb:(commonApp: CommonApp)->Unit)
    : RecyclerView.Adapter<SelectRecyclerViewAdapter.ViewHolder>() {

    var cApps = mutableListOf<CommonApp>()

    val TAG = "SelectRecyclerViewAdapter#$#"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemSelectBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding,cb)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
            = holder.bind(cApps[position])

    override fun getItemCount(): Int = cApps.size

    fun replaceData(arrayList: List<CommonApp>) {

        cApps.clear()
        cApps.addAll(arrayList)
        notifyDataSetChanged()
    }

    class ViewHolder(private var binding: ItemSelectBinding, var cb:(commonApp: CommonApp)->Unit) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cApps_: CommonApp) {



            if(cApps_.isExcept) {

                val cae = CAppException.values().find { it.get == cApps_.pkgName }?:return

                binding.ivApp.setImageResource(cae.rss)
                binding.tvApp.text = cae.title

            } else {
                binding.ivApp.setImageDrawable(App.get.packageManager.getApplicationIcon(cApps_.pkgName))
                binding.tvApp.text = cApps_.label
            }


            binding.root.setOnClickListener { _ ->
                cb.invoke(cApps_)
            }

            binding.executePendingBindings()
        }
    }

}