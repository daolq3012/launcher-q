package seoft.co.kr.launcherq.ui.arrange

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_arrange.*
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.databinding.ActivityArrangeBinding
import seoft.co.kr.launcherq.ui.QuickImageAdapter
import seoft.co.kr.launcherq.utill.observeActMsg

class ArrangeActivity : AppCompatActivity() {

    val TAG = "ArrangeActivity#$#"

    private lateinit var binding: ActivityArrangeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_arrange)

        val vm = ViewModelProviders.of(this,ArrangeViewModel(Repo).create()).get(ArrangeViewModel::class.java)
        binding.vm = vm
        binding.executePendingBindings()

        vm.observeActMsg(this, Observer {
            when(it) {

            }
        })

        vm.liveDataApps.observe(this,
            Observer {
                it?.let {
                    val gridCnt = vm.gridCnt
                    gvApps.numColumns = gridCnt
                    gvApps.adapter = QuickImageAdapter(
                        this,
                        100, // 100 dp
                        it.map { it.commonApp }.take(gridCnt * gridCnt).toMutableList()
                    )
                }
            })

        vm.start()

        val initDir = intent.getIntExtra(DIR,0)
        vm.pkgName = intent.getStringExtra(PKG_NAME)
        vm.label = intent.getStringExtra(LABEL)
        vm.detailName = intent.getStringExtra(DETAIL_NAME)

        vm.refreshApp(initDir)

    }


    companion object {
        val DIR = "DIR"
        val PKG_NAME = "PKG_NAME"
        val LABEL = "LABEL"
        val DETAIL_NAME = "DETAIL_NAME"
    }


}