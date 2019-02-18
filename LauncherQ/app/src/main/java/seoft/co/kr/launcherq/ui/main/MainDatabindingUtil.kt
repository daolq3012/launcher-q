package seoft.co.kr.launcherq.ui.main

import android.content.pm.LauncherApps
import android.databinding.BindingAdapter
import android.os.Process
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import seoft.co.kr.launcherq.data.model.Command
import seoft.co.kr.launcherq.data.model.QuickApp
import seoft.co.kr.launcherq.data.model.QuickAppType
import seoft.co.kr.launcherq.data.model.toCommonApp


@BindingAdapter("visibiltyTwoStepBg")
fun setVisibiltyTwoStepBg(ll: LinearLayout, twoStepApp: QuickApp) {
    if(twoStepApp.type == QuickAppType.EMPTY) ll.visibility = View.INVISIBLE
    else ll.visibility = View.VISIBLE
}

@BindingAdapter("visibiltyTwoStepItem","pos", "twoAppList")
fun setVisibiltyTwoStepItem(ll: LinearLayout, twoStepApp: QuickApp, pos:Int, twoAppList:List<Command>) {

    when (twoStepApp.type) {
        QuickAppType.ONE_APP, QuickAppType.EMPTY -> return
        QuickAppType.FOLDER -> {
            ll.visibility = if(twoStepApp.cmds.size > pos) View.VISIBLE
            else View.GONE
        }
        QuickAppType.TWO_APP -> {
            ll.visibility = if(twoAppList.size > pos) View.VISIBLE
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

@BindingAdapter("text","pos","twoAppList")
fun setTextTwoStepItem(tv: TextView, qApp: QuickApp, pos:Int, twoAppList:List<Command>) {

    when (qApp.type) {
        QuickAppType.EMPTY -> return
        QuickAppType.FOLDER -> {
            with(qApp.cmds){
                if(this.size > pos) tv.text = this[pos].toCommonApp().label
            }
        }
        QuickAppType.TWO_APP -> {
            with(twoAppList){
                if(size > pos ) tv.text = this[pos].title
            }
        }
        QuickAppType.EXPERT -> {
            with(qApp.expert!!.useTwo){
                if(this!![pos] != null) tv.text = this[pos]!!.name
//                if(this!!.size > pos) tv.text = this[pos]!!.name
            }
        }
        QuickAppType.ONE_APP -> {

            val shortcutQuery = LauncherApps.ShortcutQuery().apply {
                setQueryFlags(
                    LauncherApps.ShortcutQuery.FLAG_MATCH_DYNAMIC or
                            LauncherApps.ShortcutQuery.FLAG_MATCH_MANIFEST or
                            LauncherApps.ShortcutQuery.FLAG_MATCH_PINNED)
                setPackage(qApp.commonApp.pkgName)

            }

            with(MainActivity.launcherApps.getShortcuts(shortcutQuery, Process.myUserHandle())!!){
                if(this.size > pos) tv.text = this[pos].shortLabel
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


