package seoft.co.kr.launcherq.ui.drawer

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.ComponentName
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_drawer.*
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.data.model.CommonApp
import seoft.co.kr.launcherq.databinding.ActivityDrawerBinding
import seoft.co.kr.launcherq.ui.MsgType
import seoft.co.kr.launcherq.ui.main.DrawerAppAdapter
import seoft.co.kr.launcherq.utill.SC
import seoft.co.kr.launcherq.utill.i
import seoft.co.kr.launcherq.utill.observeActMsg
import seoft.co.kr.launcherq.utill.toast

class DrawerActivity : AppCompatActivity() {

    val TAG = "DrawerActivity#$#"

    private lateinit var binding: ActivityDrawerBinding
    lateinit var viewPagerAdapter: DrawerPagerAdapter
    var recyclerViews:ArrayList<RecyclerView> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_drawer)

        val vm = ViewModelProviders.of(this,DrawerViewModel(Repo).create()).get(DrawerViewModel::class.java)
        binding.vm = vm
        binding.executePendingBindings()

        vm.observeActMsg(this, Observer {
            when(it) {
                MsgType.UPDATE_APPS ->updateApps(vm.msg as MutableList<CommonApp>)

            }
        })


        initViews()
        vm.start()
    }

    fun initViews(){
        viewPagerAdapter = DrawerPagerAdapter(recyclerViews)
        vpDrawer.adapter = viewPagerAdapter
    }

    fun updateApps(dApps: MutableList<CommonApp>) {
        val pageSize = if (dApps.size % SC.ITEM_GRID_NUM == 0)
            dApps.size / SC.ITEM_GRID_NUM
        else
            dApps.size / SC.ITEM_GRID_NUM + 1

        for (i in 0 until pageSize) {
            val rv = RecyclerView(this)
            val lm = GridLayoutManager(this, SC.NUMBER_OF_COLUMNS)
            val appAdapter = DrawerAppAdapter(dApps, i) {

                it.toString().toast()
                it.toString().i(TAG)

                launchApp(it)
            }

            rv.layoutManager = lm
            rv.adapter = appAdapter
            recyclerViews.add(rv)
            viewPagerAdapter.notifyDataSetChanged()
        }
    }

    fun launchApp(cApp:CommonApp) {

        // launch app
        applicationContext.startActivity(
            Intent(Intent.ACTION_MAIN)
                .apply {
                    addCategory(Intent.CATEGORY_LAUNCHER)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                    component = ComponentName(cApp.pkgName, cApp.detailName)
                }
        )
    }









    override fun finish() {
        super.finish()
        overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up )

    }




}
