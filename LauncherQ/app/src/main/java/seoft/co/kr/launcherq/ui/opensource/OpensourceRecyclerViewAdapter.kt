package seoft.co.kr.launcherq.ui.opensource


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import seoft.co.kr.launcherq.data.model.Opensource
import seoft.co.kr.launcherq.databinding.ItemOpensourceBinding


class OpensourceRecyclerViewAdapter(private var notice: List<Opensource>)
    : RecyclerView.Adapter<OpensourceRecyclerViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val binding = ItemOpensourceBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
            = holder.bind(notice[position])

    override fun getItemCount(): Int = notice.size

    fun replaceData(arrayList: List<Opensource>) {
        notice = arrayList
        notifyDataSetChanged()
    }

    class ViewHolder(private var binding: ItemOpensourceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(os_: Opensource) {
            binding.os = os_

            binding.executePendingBindings()
        }
    }



}