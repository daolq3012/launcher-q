package seoft.co.kr.launcherq.ui.arrange

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_arrange.*
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.data.model.CommonApp
import seoft.co.kr.launcherq.data.model.QuickAppType
import seoft.co.kr.launcherq.databinding.ActivityArrangeBinding
import seoft.co.kr.launcherq.ui.MsgType
import seoft.co.kr.launcherq.ui.select.SelectActivity
import seoft.co.kr.launcherq.utill.observeActMsg
import seoft.co.kr.launcherq.utill.showDialog

class ArrangeActivity : AppCompatActivity() {

    val TAG = "ArrangeActivity#$#"

    private lateinit var binding: ActivityArrangeBinding
    val APP_SELECT_REQ_CODE = 1234

    lateinit var vm : ArrangeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_arrange)

        vm = ViewModelProviders.of(this,ArrangeViewModel(Repo).create()).get(ArrangeViewModel::class.java)
        binding.vm = vm
        binding.executePendingBindings()

        vm.observeActMsg(this, Observer {
            when(it) {
                MsgType.SELECT_APP -> {
                    val intent = Intent(applicationContext,SelectActivity::class.java)
                    startActivityForResult(intent,APP_SELECT_REQ_CODE)
                }
            }
        })

        vm.liveDataApps.observe(this,
            Observer {
                it?.let {
                    val gridCnt = vm.gridCnt
                    gvApps.numColumns = gridCnt
                    gvApps.adapter = ArrangeImageAdapter(
                        this,
                        it.take(gridCnt * gridCnt).toMutableList()
                    ){
                        if(vm.isMoving) {
                            if(it.quickApp.type == QuickAppType.EMPTY)
                                vm.moveApp(it.pos)
                            else {
                                AlertDialog.Builder(this).showDialog(
                                    message = "있는데 덮어 씌우겠습니까?",
                                    postiveBtText = "네",
                                    negativeBtText = "아니요",
                                    cbPostive = {
                                        vm.moveApp(it.pos)
                                    },
                                    cbNegative = {
                                        vm.cancelMoveApp()
                                    })
                            }
                        }
                        else vm.setPickedApp(it.quickApp, it.pos)
                    }
                }
            })

        vm.start()

        vm.dir = intent.getIntExtra(DIR,0)

        with(intent){
            getStringExtra(PKG_NAME)?.let {
                vm.insertingApp = CommonApp(
                    getStringExtra(PKG_NAME),
                    getStringExtra(LABEL),
                    getStringExtra(DETAIL_NAME),
                    false)
            }
        }

        vm.refreshAppGrid()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(resultCode == Activity.RESULT_OK && requestCode == APP_SELECT_REQ_CODE) {
            data?.run {
                vm.saveAppToCurPos(
                    CommonApp(
                        getStringExtra(PKG_NAME),
                        getStringExtra(LABEL),
                        getStringExtra(DETAIL_NAME),
                        false))
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        val DIR = "DIR"
        val PKG_NAME = "PKG_NAME"
        val LABEL = "LABEL"
        val DETAIL_NAME = "DETAIL_NAME"
    }


}

// TODO LIST
// 그리드뷰 내에서 클릭해서 현 클릭상황 알 수 있게 (ui로도 표시) - ok
//        3. 그리드 크기 적당히
//          4. 버튼 여러상황일때 누를수있거나없거나 데이터넣거나 뺴거나 저장하거나