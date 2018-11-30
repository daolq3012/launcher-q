package seoft.co.kr.launcherq.ui.drawer

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.databinding.ActivityDrawerBinding
import seoft.co.kr.launcherq.utill.observeActMsg

class DrawerActivity : AppCompatActivity() {

    val TAG = "DrawerActivity#$#"

    private lateinit var binding: ActivityDrawerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_drawer)

        val vm = ViewModelProviders.of(this,DrawerViewModel(Repo).create()).get(DrawerViewModel::class.java)
        binding.vm = vm
        binding.executePendingBindings()

        vm.observeActMsg(this, Observer {
            when(it) {






            }
        })

        vm.start()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up )

    }




}
