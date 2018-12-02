package seoft.co.kr.launcherq.ui.drawer

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.ComponentName
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Point
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_drawer.*
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.data.model.CommonApp
import seoft.co.kr.launcherq.databinding.ActivityDrawerBinding
import seoft.co.kr.launcherq.ui.MsgType
import seoft.co.kr.launcherq.utill.SelectorDialog
import seoft.co.kr.launcherq.utill.i
import seoft.co.kr.launcherq.utill.observeActMsg
import seoft.co.kr.launcherq.utill.toast

class DrawerActivity : AppCompatActivity() {

    val TAG = "DrawerActivity#$#"

    // ref : http://iw90.tistory.com/238
    // finish drawer when scroll bottom to top
    // distance is device size /  FINISH_ACTION_SENSITIVE(6)
    val FINISH_ACTION_SENSITIVE = 6

    private lateinit var binding: ActivityDrawerBinding

    lateinit var vm : DrawerViewModel

    lateinit var viewPagerAdapter: DrawerPagerAdapter
    var recyclerViews:ArrayList<RecyclerView> = arrayListOf()

    var screenSize : Point = Point()

    var befY = 0
    var afterY = 0

    var drawerMode = DrawerMode.LAUNCH_MODE
    lateinit var selectedApp : CommonApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_drawer)

        vm = ViewModelProviders.of(this,DrawerViewModel(Repo).create()).get(DrawerViewModel::class.java)
        binding.vm = vm
        binding.executePendingBindings()

        vm.observeActMsg(this, Observer {
            when(it) {
                MsgType.UPDATE_APPS -> updateApps(vm.msg as DrawerLoadInfo)
                MsgType.SHOW_SETTING_DAILOG -> { showSettingDialog() }

            }
        })




        initViews()
        vm.start()
    }



    fun initViews(){

        windowManager.defaultDisplay.getRealSize( screenSize )
        viewPagerAdapter = DrawerPagerAdapter(recyclerViews)
        vpDrawer.adapter = viewPagerAdapter
    }


    fun updateApps(drawerLoadInfo:DrawerLoadInfo) {

        recyclerViews.clear()

        drawerLoadInfo.let {
            val pageSize = if (it.dApps.size % it.itemGridNum == 0)
                it.dApps.size / it.itemGridNum
            else
                it.dApps.size / it.itemGridNum + 1

            for (i in 0 until pageSize) {
                val rv = RecyclerView(this)
                val lm = GridLayoutManager(this, it.columnNum)
                val appAdapter = DrawerGridAdapter(it.dApps, i, it.itemGridNum,
                    {
                        "clickApp ${it}".i(TAG)
                        clickApp(it)
                    },
                    {
                        "longClickApp ${it}".i(TAG)
                        longClickApp(it)
                    })

                rv.layoutManager = lm
                rv.adapter = appAdapter
//            rv.addItemDecoration(SPID(100))
                rv.addItemDecoration(GridSpacingItemDecoration(it.columnNum, 50))

                rv.setOnTouchListener { view, motionEvent ->
                    when(motionEvent.action) {
                        MotionEvent.ACTION_DOWN -> {
                            befY = motionEvent.y.toInt()
                        }
                        MotionEvent.ACTION_UP -> {
                            afterY = motionEvent.y.toInt()
                            if(befY - afterY > screenSize.y / FINISH_ACTION_SENSITIVE )
                                finish()
                        }
                    }
                    false
                }

                recyclerViews.add(rv)
            }
        }

        viewPagerAdapter.notifyDataSetChanged()

    }

    private fun clickApp(dApp: CommonApp) {
        when(drawerMode) {
            DrawerMode.LAUNCH_MODE -> {
                launchApp(dApp)
            }
            DrawerMode.ADD_MODE -> {

            }
            DrawerMode.MOVE_MODE -> {
                vm.moveApp(selectedApp.pkgName,dApp.pkgName)
                drawerMode = DrawerMode.LAUNCH_MODE
            }
            DrawerMode.HIDE_MODE -> {
                vm.setUnhideApp(dApp.pkgName)
                "${dApp.label}앱의 숨기기가 해제되었습니다".toast(Toast.LENGTH_SHORT)
            }
        }
    }

    private fun longClickApp(dApp: CommonApp) {

        SelectorDialog(context = this,
            title = "설정",
            firstSelector = SelectorDialog.DialogSelectorInfo("바로가기 추가"),
            secondSelector = SelectorDialog.DialogSelectorInfo("순서 변경"),
            thirdSelector =  SelectorDialog.DialogSelectorInfo("앱 숨기기"),
            cb = {
                when(it) {
                    1 -> {
                        selectedApp = dApp
                        drawerMode = DrawerMode.ADD_MODE
                    }
                    2 -> {
                        selectedApp = dApp
                        drawerMode = DrawerMode.MOVE_MODE
                        "이동할 곳을 선택해주세요".toast()
                    }
                    3 -> {
                        vm.setHideApp(dApp.pkgName)
                    }
                    else -> {}
                }
            }).create()

    }

    private fun showSettingDialog() {
        val dsd = DrawerSettingDialog(this,drawerMode,Repo) {

            when(it) {
                DrawerSettingDialog.DrawerSettingDialogResult.OK -> {
                    vm.loadDrawerList(drawerMode == DrawerMode.HIDE_MODE)
                }
                DrawerSettingDialog.DrawerSettingDialogResult.SET_HIDE_APP -> {

                    val isCurHideMode = drawerMode == DrawerMode.HIDE_MODE
                    vm.loadDrawerList(!isCurHideMode)
                    drawerMode =
                            if(isCurHideMode) DrawerMode.LAUNCH_MODE
                            else DrawerMode.HIDE_MODE
                }
            }
        }
        dsd.show()
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

    override fun onRestart() {
        super.onRestart()

        vm.onRestartInVM()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up )
    }

    override fun onBackPressed() {

        if(drawerMode == DrawerMode.HIDE_MODE){
            vm.loadDrawerList(false)
            drawerMode = DrawerMode.LAUNCH_MODE
        }
        else
            super.onBackPressed()

    }

    inner class GridSpacingItemDecoration(
        private val spanCount: Int,
        private val spacing: Int
    ) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val position = parent.getChildAdapterPosition(view) // item position
            val column = position % spanCount // item column

            outRect.left = spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)

            if (position < spanCount) { // top edge
                outRect.top = spacing
            }
            outRect.bottom = ( spacing * 1.6 ).toInt() // item bottom
        }
    }

    enum class DrawerMode{
        LAUNCH_MODE,
        ADD_MODE,
        MOVE_MODE,
        HIDE_MODE
    }

}
