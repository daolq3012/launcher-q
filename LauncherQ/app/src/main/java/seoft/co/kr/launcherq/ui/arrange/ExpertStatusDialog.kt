package seoft.co.kr.launcherq.ui.arrange

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.TextView
import kotlinx.android.synthetic.main.dialog_expert_status.*
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.model.QuickApp

class ExpertStatusDialog(context: Context, val quickApp: QuickApp, val cb:(ExpertManageCommand)->Unit ) : Dialog(context) {

    val TAG = "ExpertStatusDialog#$#"

    lateinit var tvAdds : Array<TextView>
    lateinit var tvEdits : Array<TextView>
    lateinit var tvDels : Array<TextView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_expert_status)

        initView()
        initListener()

    }

    fun initView(){

        tvAdds = arrayOf(tvAddTwo0,tvAddTwo1,tvAddTwo2,tvAddTwo3,tvAddTwo4,tvAddTwo5)
        tvEdits = arrayOf(tvEditTwo0,tvEditTwo1,tvEditTwo2,tvEditTwo3,tvEditTwo4,tvEditTwo5)
        tvDels = arrayOf(tvDelTwo0,tvDelTwo1,tvDelTwo2,tvDelTwo3,tvDelTwo4,tvDelTwo5)


        quickApp.expert?.let {

            if(it.useOne == null ) {
                tvAddOne.visibility = View.VISIBLE
                tvEditOne.visibility = View.GONE
                tvDelOne.visibility = View.GONE
            } else {
                tvAddOne.visibility = View.GONE
                tvEditOne.visibility = View.VISIBLE
                tvDelOne.visibility = View.VISIBLE
            }

            it.useTwo?.let {

                it.forEachIndexed { i, customIntent ->

                    if(customIntent == null/*.name.isNotEmpty()*/) {
                        tvAdds[i].visibility = View.VISIBLE
                        tvEdits[i].visibility = View.GONE
                        tvDels[i].visibility = View.GONE
                    } else {
                        tvAdds[i].visibility = View.GONE
                        tvEdits[i].visibility = View.VISIBLE
                        tvDels[i].visibility = View.VISIBLE
                    }
                }

            } ?: let {
                tvAdds.forEach { it.visibility = View.VISIBLE }
                tvEdits.forEach { it.visibility = View.GONE }
                tvDels.forEach { it.visibility = View.GONE }
            }

        } ?: let {
            tvAddOne.visibility = View.VISIBLE
            tvEditOne.visibility = View.GONE
            tvDelOne.visibility = View.GONE
            tvAdds.forEach { it.visibility = View.VISIBLE }
            tvEdits.forEach { it.visibility = View.GONE }
            tvDels.forEach { it.visibility = View.GONE }
        }

    }

    fun initListener(){

        tvAddOne.setOnClickListener { v -> done(0,0) }
        tvEditOne.setOnClickListener { v -> done(0,1) }
        tvDelOne.setOnClickListener { v -> done(0,2) }

        tvAdds.forEachIndexed { i, tv -> tv.setOnClickListener { v -> done(i+1,0) } }
        tvEdits.forEachIndexed { i, tv -> tv.setOnClickListener { v -> done(i+1,1) } }
        tvDels.forEachIndexed { i, tv -> tv.setOnClickListener { v -> done(i+1,2) } }


    }

    fun done(pos:Int,cmdType:Int){
        cb.invoke(ExpertManageCommand(pos,cmdType))
        dismiss()
    }

    data class ExpertManageCommand(
        // oneStep is 0 , else twoStep is 1~6
        var pos : Int,

        /**
         * 0 add
         * 1 edit
         * 2 del
         */
        var cmdType : Int

    )

}