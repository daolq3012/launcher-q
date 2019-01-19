package seoft.co.kr.launcherq.ui.setting

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.SeekBar
import kotlinx.android.synthetic.main.dialog_select_number.*
import seoft.co.kr.launcherq.R


class SelectNumberDialog(context:Context, val title:String,var unit:String, var curVal:Int, val minVal:Int,
                         val maxVal:Int, val cb:(Int)->Unit ) : Dialog(context) {

    val TAG = "SelectNumberDialog#$#"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_select_number)

        inits()
    }

    fun inits(){

        tvTitle.text = "$title ($minVal ~ $maxVal)"
        tvNumber.text = "$curVal $unit"
        sbNumber.progress = curVal - minVal
        sbNumber.max = maxVal - minVal

        tvTitle.setOnClickListener { v ->
            dismiss()
        }

        tvNumber.setOnClickListener { v ->
            dismiss()
        }

        sbNumber.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(sb: SeekBar, progress: Int, b: Boolean) {
                curVal = progress + minVal
                tvNumber.text = "${curVal} $unit"
            }

            override fun onStartTrackingTouch(p0: SeekBar) {}
            override fun onStopTrackingTouch(p0: SeekBar) {}
        })

        tvOk.setOnClickListener { v ->
            cb.invoke(curVal)
            dismiss()
        }

        tvCancel.setOnClickListener { v ->
            dismiss()
        }

    }

}