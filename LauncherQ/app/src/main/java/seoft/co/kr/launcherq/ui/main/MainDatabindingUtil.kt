package seoft.co.kr.launcherq.ui.main

import android.databinding.BindingAdapter
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import seoft.co.kr.launcherq.data.model.QuickApp
import seoft.co.kr.launcherq.data.model.QuickAppType
import seoft.co.kr.launcherq.data.model.toCommonApp


@BindingAdapter("visibiltyTwoStepBg")
fun setVisibiltyTwoStepBg(ll: LinearLayout, twoStepApp: QuickApp) {
    if(twoStepApp.type == QuickAppType.EMPTY) ll.visibility = View.INVISIBLE
    else ll.visibility = View.VISIBLE
}

@BindingAdapter("visibiltyTwoStepItem","pos")
fun setVisibiltyTwoStepItem(ll: LinearLayout, twoStepApp: QuickApp, pos:Int) {

    when (twoStepApp.type) {
        QuickAppType.ONE_APP, QuickAppType.EMPTY -> return
        QuickAppType.FOLDER, QuickAppType.TWO_APP -> {
            ll.visibility = if(twoStepApp.cmds.size > pos) View.VISIBLE
            else View.GONE
        }
        QuickAppType.EXPERT -> {

            with(twoStepApp.expert!!.useTwo) {
                ll.visibility = if(this!![pos] != null) View.VISIBLE else View.GONE
            }

//            ll.visibility = if(twoStepApp.expert!!.useTwo!!.size > pos) View.VISIBLE
//            else View.GONE
        }
    }
}

@BindingAdapter("text","pos")
fun setTextTwoStepItem(tv: TextView, twoStepApp: QuickApp, pos:Int) {

    when (twoStepApp.type) {
        QuickAppType.ONE_APP, QuickAppType.EMPTY -> return
        QuickAppType.FOLDER, QuickAppType.TWO_APP -> {
            with(twoStepApp.cmds){
                if(this.size > pos) tv.text = this[pos].toCommonApp().label
            }
        }
        QuickAppType.EXPERT -> {
            with(twoStepApp.expert!!.useTwo){
                if(this!![pos] != null) tv.text = this[pos]!!.name
//                if(this!!.size > pos) tv.text = this[pos]!!.name
            }
        }
    }
}

@BindingAdapter("step","stepView")
fun setVisibilty(view:View, step:Step, stepView:StepView) {

    when(stepView) {
        StepView.APP_STARTER -> {
            view.visibility = if(step == Step.TOUCH_START) View.VISIBLE
            else View.INVISIBLE
        }
        StepView.ONE_STEP -> {
            view.visibility = if(step == Step.OPEN_ONE) View.VISIBLE
            else View.INVISIBLE
        }
        StepView.TWO_STEP -> {
            view.visibility = if(step == Step.OPEN_TWO) View.VISIBLE
            else View.INVISIBLE
        }
    }
}


