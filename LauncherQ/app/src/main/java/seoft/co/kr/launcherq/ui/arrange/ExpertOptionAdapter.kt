package seoft.co.kr.launcherq.ui.arrange

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import seoft.co.kr.launcherq.data.model.ExpertOption
import seoft.co.kr.launcherq.databinding.ItemExpertOptionBinding

class ExpertOptionAdapter(var options : List<ExpertOption> ,var cb:(result: ExpertOption)->Unit)
    : RecyclerView.Adapter<ExpertOptionAdapter.ViewHolder>() {

    val TAG = "ExpertOptionAdapter#$#"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemExpertOptionBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding,cb)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
            = holder.bind(options[position])

    override fun getItemCount(): Int = options.size

    class ViewHolder(private var binding: ItemExpertOptionBinding, var cb:(result: ExpertOption)->Unit) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(expertOption: ExpertOption) {

            binding.tvExeprtSetting.text = expertOption.name

            binding.root.setOnClickListener { _ ->
                cb.invoke(expertOption)
            }

            binding.executePendingBindings()
        }
    }

}