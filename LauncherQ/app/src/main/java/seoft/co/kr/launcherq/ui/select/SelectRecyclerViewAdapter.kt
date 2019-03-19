package seoft.co.kr.launcherq.ui.select

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.data.model.CAppException
import seoft.co.kr.launcherq.data.model.CommonApp
import seoft.co.kr.launcherq.databinding.ItemSelectBinding
import seoft.co.kr.launcherq.utill.App

class SelectRecyclerViewAdapter(val repo:Repo, var showLabelOptions:Boolean, var cb:(commonApp: CommonApp)->Unit)
    : RecyclerView.Adapter<SelectRecyclerViewAdapter.ViewHolder>() {

    var cApps = mutableListOf<CommonApp>()

    val TAG = "SelectRecyclerViewAdapter#$#"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemSelectBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(repo,binding,showLabelOptions,cb)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
            = holder.bind(cApps[position])

    override fun getItemCount(): Int = cApps.size

    fun replaceData(arrayList: List<CommonApp>) {

        cApps.clear()
        cApps.addAll(arrayList)
        notifyDataSetChanged()
    }

    class ViewHolder(val repo:Repo, private var binding: ItemSelectBinding, var showLabelOptions:Boolean, var cb:(commonApp: CommonApp)->Unit) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cApps_: CommonApp) {
            if(cApps_.isExcept) {

                val cae = CAppException.values().find { it.get == cApps_.pkgName }?:return

                binding.ivApp.setImageResource(cae.rss)
                if(showLabelOptions) {
                    binding.tvApp.visibility = View.VISIBLE
                    binding.tvApp.text = cae.title
                }
                else {
                    binding.tvApp.visibility = View.GONE
                }

            } else {
                binding.ivApp.setImageDrawable(
                    if(repo.imageCacheRepo.containsKey(cApps_.pkgName)) repo.imageCacheRepo.getDrawable(cApps_.pkgName)
                    else App.get.packageManager.getApplicationIcon(cApps_.pkgName)
                )
                if(showLabelOptions) {
                    binding.tvApp.visibility = View.VISIBLE
                    binding.tvApp.text = cApps_.label
                }
                else {
                    binding.tvApp.visibility = View.GONE
                }
            }

            binding.root.setOnClickListener { _ ->
                cb.invoke(cApps_)
            }

            binding.executePendingBindings()
        }
    }

}