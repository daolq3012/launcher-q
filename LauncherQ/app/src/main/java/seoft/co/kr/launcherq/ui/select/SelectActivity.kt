package seoft.co.kr.launcherq.ui.select

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.databinding.ActivitySelectBinding
import seoft.co.kr.launcherq.ui.arrange.ArrangeActivity.Companion.DETAIL_NAME
import seoft.co.kr.launcherq.ui.arrange.ArrangeActivity.Companion.LABEL
import seoft.co.kr.launcherq.ui.arrange.ArrangeActivity.Companion.PKG_NAME
import seoft.co.kr.launcherq.utill.observeActMsg
import seoft.co.kr.launcherq.utill.setupActionBar

class SelectActivity : AppCompatActivity() {

    val TAG = "SelectActivity#$#"

    private lateinit var binding: ActivitySelectBinding
    private val selectRecyclerViewAdapter = SelectRecyclerViewAdapter {
        val rstIntent = Intent()
            .apply {
                putExtra(PKG_NAME,it.pkgName)
                putExtra(DETAIL_NAME,it.detailName)
                putExtra(LABEL,it.label)
            }

        setResult(Activity.RESULT_OK,rstIntent)
        finish()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_select)

        val vm = ViewModelProviders.of(this,SelectViewModel(Repo).create()).get(SelectViewModel::class.java)
        binding.vm = vm
        binding.executePendingBindings()

        vm.observeActMsg(this, Observer {
            when(it) {

            }
        })

        binding.gvApps.layoutManager = GridLayoutManager(this,6)
        binding.gvApps.adapter = selectRecyclerViewAdapter
        vm.liveDataCommonApps.observe(this,
            Observer { it?.let {
                selectRecyclerViewAdapter.replaceData(it)
            } })


        vm.start()

        initToolbar()
    }


    private fun initToolbar(){
        setupActionBar(R.id.toolbar) {
            setHomeAsUpIndicator(R.drawable.ic_arrow_back_white)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


}