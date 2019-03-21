package seoft.co.kr.launcherq.ui.opensource

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.databinding.ActivityOpensourceBinding
import seoft.co.kr.launcherq.ui.MsgType
import seoft.co.kr.launcherq.utill.observeActMsg
import seoft.co.kr.launcherq.utill.setupActionBar

class OpensourceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOpensourceBinding

    private val opensourceRecyclerViewAdapter = OpensourceRecyclerViewAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_opensource)

        val vm = ViewModelProviders.of(this, OpensourceViewModel(Repo).create()).get(OpensourceViewModel::class.java)
        binding.vm = vm
        binding.executePendingBindings()


        vm.observeActMsg(this, Observer {
            when(it) {
                MsgType.FINISH_ACTIVITY -> finish()
            }
        })

        binding.rvOpensources.layoutManager = LinearLayoutManager(this)
        binding.rvOpensources.adapter = opensourceRecyclerViewAdapter
        vm.livedOpensources.observe(this,
            Observer {
                it?.let {
                    opensourceRecyclerViewAdapter.replaceData(it)
                }
            })
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
