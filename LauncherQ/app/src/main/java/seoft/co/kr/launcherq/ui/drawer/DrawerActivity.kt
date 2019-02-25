package seoft.co.kr.launcherq.ui.drawer

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Point
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_drawer.*
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.data.model.CommonApp
import seoft.co.kr.launcherq.databinding.ActivityDrawerBinding
import seoft.co.kr.launcherq.ui.MsgType
import seoft.co.kr.launcherq.ui.arrange.ArrangeActivity
import seoft.co.kr.launcherq.ui.drawer.DrawerViewModel.DrawerMode
import seoft.co.kr.launcherq.utill.*


class DrawerActivity : AppCompatActivity() {

    val TAG = "DrawerActivity#$#"
    val UNINSTALL_REQUEST_CODE = 101
    lateinit var uninstallingPkgName : String

    private lateinit var binding: ActivityDrawerBinding

    lateinit var vm : DrawerViewModel

    lateinit var viewPagerAdapter: DrawerPagerAdapter
    var recyclerViews:ArrayList<RecyclerView> = arrayListOf()

    var screenSize : Point = Point()

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
                MsgType.CLEAR_SEARCH_EDIT_TEXT -> etSearch.setText("")
            }
        })

        initViews()
        vm.start()
    }



    fun initViews(){

        windowManager.defaultDisplay.getRealSize( screenSize )
        viewPagerAdapter = DrawerPagerAdapter(recyclerViews)
        vpDrawer.adapter = viewPagerAdapter
        rlRoot.background = BitmapDrawable(App.get.resources,SC.bgBitmap!!)
    }


    fun updateApps(drawerLoadInfo:DrawerLoadInfo) {

        recyclerViews.clear()

        drawerLoadInfo.let {
            val pageSize = if (it.dApps.size % it.itemGridNum == 0) it.dApps.size / it.itemGridNum
            else it.dApps.size / it.itemGridNum + 1

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
                rv.addItemDecoration(GridSpacingItemDecoration(it.columnNum, 30))

//                rv.setOnTouchListener { view, motionEvent ->
//                    when(motionEvent.action) {
//                        MotionEvent.ACTION_DOWN -> {
//                            befY = motionEvent.y.toInt()
//                        }
//                        MotionEvent.ACTION_UP -> {
//                            afterY = motionEvent.y.toInt()
//                            if(befY - afterY > screenSize.y / FINISH_ACTION_SENSITIVE )
//                                finish()
//                        }
//                    }
//                    false
//                }

                recyclerViews.add(rv)
            }
        }

        viewPagerAdapter.notifyDataSetChanged()

    }

    private fun clickApp(dApp: CommonApp) {
        when(vm.drawerMode) {
            DrawerMode.LAUNCH_MODE -> {
                launchApp(dApp)
            }
            DrawerMode.ADD_MODE -> {

            }
            DrawerMode.MOVE_MODE -> {
                vm.moveApp(selectedApp.pkgName,dApp.pkgName)
                vm.drawerMode = DrawerMode.LAUNCH_MODE
            }
            DrawerMode.HIDE_MODE -> {
                vm.setUnhide(dApp.pkgName)
                "${dApp.label}앱의 숨기기가 해제되었습니다".toast(Toast.LENGTH_SHORT)
            }
        }
    }

    private fun longClickApp(dApp: CommonApp) {

        if(vm.drawerMode == DrawerMode.HIDE_MODE) return

        DrawerAppSettingDialog(this,Repo){
            when(it) {
                DrawerAppSettingDialog.DrawerAppSettingType.SET_ORDER -> {
                    selectedApp = dApp
                    vm.drawerMode = DrawerMode.MOVE_MODE
                    "이동할 곳을 선택해주세요".toast()
                }
                DrawerAppSettingDialog.DrawerAppSettingType.SET_HIDE -> {
                    vm.setHide(dApp.pkgName)
                    "${dApp.label}앱을 숨겼습니다"
                }
                DrawerAppSettingDialog.DrawerAppSettingType.SET_TOP -> startArrangeActivity(0,dApp)
                DrawerAppSettingDialog.DrawerAppSettingType.SET_RIGHT -> startArrangeActivity(1,dApp)
                DrawerAppSettingDialog.DrawerAppSettingType.SET_BOTTOM -> startArrangeActivity(2,dApp)
                DrawerAppSettingDialog.DrawerAppSettingType.SET_LEFT -> startArrangeActivity(3,dApp)
                DrawerAppSettingDialog.DrawerAppSettingType.SET_REMOVE -> removeApp(dApp.pkgName)
            }
        }.show()

    }

    fun removeApp(pkgName:String) {
        uninstallingPkgName = pkgName
        val intent = Intent(Intent.ACTION_UNINSTALL_PACKAGE).
            apply {
                data = Uri.parse("package:$pkgName")
                putExtra(Intent.EXTRA_RETURN_RESULT,true)
            }
        startActivityForResult(intent,UNINSTALL_REQUEST_CODE)
    }

    fun startArrangeActivity(dir:Int, app: CommonApp) {

        val intent = Intent(applicationContext,ArrangeActivity::class.java)
            .apply {
                putExtra(ArrangeActivity.DIR,dir)
                putExtra(ArrangeActivity.PKG_NAME,app.pkgName)
                putExtra(ArrangeActivity.LABEL,app.label)
//                putExtra(ArrangeActivity.DETAIL_NAME,app.detailName)
            }

        startActivity(intent)

    }

    private fun showSettingDialog() {
        DrawerWholeSettingDialog(this,vm.drawerMode,Repo) {

            when(it) {
                DrawerWholeSettingDialog.DrawerSettingDialogResult.OK -> {
                    vm.loadDrawerList(vm.drawerMode == DrawerMode.HIDE_MODE)
                }
                DrawerWholeSettingDialog.DrawerSettingDialogResult.SET_HIDE_APP -> {

                    val isCurHideMode = vm.drawerMode == DrawerMode.HIDE_MODE
                    vm.loadDrawerList(!isCurHideMode)
                    vm.drawerMode =
                            if(isCurHideMode) DrawerMode.LAUNCH_MODE
                            else DrawerMode.HIDE_MODE
                }
            }
        }.show()
    }

    fun launchApp(cApp:CommonApp) {
        startActivity(packageManager.getLaunchIntentForPackage(cApp.pkgName))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UNINSTALL_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Log.i(TAG, "onActivityResult: user accepted the (un)install")

                // for prevent synchronized issue
                SC.drawerApps = SC.drawerApps
                    .filterNot { it.pkgName == uninstallingPkgName }
                    .toMutableList()

                vm.loadDrawerList()
            }/* else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(TAG, "onActivityResult: user canceled the (un)install")
            } else if (resultCode == Activity.RESULT_FIRST_USER) {
                Log.i(TAG, "onActivityResult: failed to (un)install")
            }*/
        }
    }

//    override fun onRestart() {
//        super.onRestart()
//
//        vm.onRestartInVM()
//    }

//    override fun finish() {
//        super.finish()
//        overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up )
//    }

    override fun onBackPressed() {

        if(vm.drawerMode == DrawerMode.HIDE_MODE){
            vm.loadDrawerList()
            vm.drawerMode = DrawerMode.LAUNCH_MODE
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
}
