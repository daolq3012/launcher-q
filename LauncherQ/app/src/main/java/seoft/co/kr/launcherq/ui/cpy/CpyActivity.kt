package seoft.co.kr.launcherq.ui.cpy

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.databinding.ActivityCpyBinding
import seoft.co.kr.launcherq.utill.observeActMsg
import seoft.co.kr.launcherq.utill.setupActionBar

class CpyActivity : AppCompatActivity() {

    val TAG = "CpyActivity#$#"

    private lateinit var binding: ActivityCpyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_cpy)

        val vm = ViewModelProviders.of(this,CpyViewModel(Repo).create()).get(CpyViewModel::class.java)
        binding.vm = vm
        binding.executePendingBindings()

        vm.observeActMsg(this, Observer {
            when(it) {

            }
        })

        vm.start()

        initToolbar()
    }


    private fun initToolbar(){
        setupActionBar(R.id.toolbar) {
            setHomeAsUpIndicator(R.drawable.ic_back)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }



}