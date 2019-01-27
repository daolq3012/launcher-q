package seoft.co.kr.launcherq.ui.arrange

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Window
import kotlinx.android.synthetic.main.dialog_expert_option.*
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.model.ExpertOption

class ExpertOptionDialog(context: Context, val expertOptions: List<ExpertOption>, val cb:(String)->Unit ) : Dialog(context) {

    val TAG = "ExpertOptionDialog#$#"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_expert_option)

        initView()
    }

    fun initView(){

        val selectRecyclerViewAdapter = ExpertOptionAdapter(expertOptions) {
            cb.invoke(it)
            dismiss()
        }

        rvExpertOptions.layoutManager = LinearLayoutManager(context)

        rvExpertOptions.adapter = selectRecyclerViewAdapter
    }

}