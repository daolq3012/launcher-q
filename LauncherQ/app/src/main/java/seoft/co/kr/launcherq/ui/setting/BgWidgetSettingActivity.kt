package seoft.co.kr.launcherq.ui.setting

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.databinding.ActivityBgWidgetSettingBinding
import seoft.co.kr.launcherq.utill.getWidget
import seoft.co.kr.launcherq.utill.i
import seoft.co.kr.launcherq.utill.observeActMsg

class BgWidgetSettingActivity : AppCompatActivity() {

    val TAG = "BgWidgetSettingActivity#$#"

    companion object {
        val WIDGET_TYPE = "WIDGET_TYPE"
    }

    private lateinit var binding: ActivityBgWidgetSettingBinding

    lateinit var widgetType : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_bg_widget_setting)

        widgetType = intent.getStringExtra(WIDGET_TYPE)

        widgetType.getWidget().getStr.i(TAG)

        val vm = ViewModelProviders.of(this, BgWidgetSettingViewModel(Repo, widgetType.getWidget()).create()).get(BgWidgetSettingViewModel::class.java)
        binding.vm = vm
        binding.executePendingBindings()

        vm.observeActMsg(this, Observer {
            when(it) {

            }
        })

        vm.start()
    }




}
