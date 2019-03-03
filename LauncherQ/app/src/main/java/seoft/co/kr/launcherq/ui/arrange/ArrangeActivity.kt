package seoft.co.kr.launcherq.ui.arrange

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_arrange.*
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.data.model.CommonApp
import seoft.co.kr.launcherq.data.model.QuickApp
import seoft.co.kr.launcherq.data.model.QuickAppType
import seoft.co.kr.launcherq.databinding.ActivityArrangeBinding
import seoft.co.kr.launcherq.ui.MsgType
import seoft.co.kr.launcherq.ui.select.SelectActivity
import seoft.co.kr.launcherq.utill.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class ArrangeActivity : AppCompatActivity() {

    val TAG = "ArrangeActivity#$#"

    private lateinit var binding: ActivityArrangeBinding
    val REQ_CODE_APP_SELECT = 1234
    val REQ_CODE_EXPERT_SETTING = 1235

    lateinit var vm : ArrangeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_arrange)

        vm = ViewModelProviders.of(this,ArrangeViewModel(Repo).create()).get(ArrangeViewModel::class.java)
        binding.vm = vm
        binding.executePendingBindings()

        vm.observeActMsg(this, Observer {
            when(it) {

                // SHOW_OPTIONS has show drawer, etc... when select app activity
                MsgType.SELECT_APP -> {
                    val intent = Intent(applicationContext,SelectActivity::class.java).apply {
                        putExtra(SelectActivity.SHOW_OPTIONS,true)
                    }
                    startActivityForResult(intent,REQ_CODE_APP_SELECT)
                }
                MsgType.SELECT_APP_IN_FOLDER -> {
                    val intent = Intent(applicationContext,SelectActivity::class.java).apply {
                        putExtra(SelectActivity.SHOW_OPTIONS,false)
                    }
                    startActivityForResult(intent,REQ_CODE_APP_SELECT)
                }
                MsgType.OPEN_FOLDER -> openFolder(vm.pickedApp.value())
                MsgType.OPEN_ICON_SETTER ->{
                    if(vm.msg as Boolean) {
                        SelectorDialog(this,
                            "선택하세요",
                            SelectorDialog.DialogSelectorInfo("수정"),
                            SelectorDialog.DialogSelectorInfo("삭제"),
                            cb = {
                                when(it) {
                                    1 -> selectIcon()
                                    2 -> {
                                        vm.removeImageCache("${vm.dir}#${vm.curPos}")
                                        vm.setHasImage(false)
                                    }
                                }
                            }
                        ).create()
                    } else {
                        selectIcon()
                    }

                }
                MsgType.OPEN_EXPERT_STATUS ->{
                    val esd = ExpertStatusDialog(this,vm.pickedApp.value()){

                        if(it.cmdType == 2) {

                            vm.deleteExpert(it.pos)

                            return@ExpertStatusDialog
                        }

                        SC.qApp4SetExpert = vm.pickedApp.value()
                        val intent = Intent(applicationContext,ExpertSettingActivity::class.java).apply {
                            putExtra(ExpertSettingActivity.ES_TYPE,it.cmdType)
                            putExtra(ExpertSettingActivity.ES_POS,it.pos)
                        }

                        startActivityForResult(intent,REQ_CODE_EXPERT_SETTING)
                    }
                    esd.show()
                }
                else -> {}
            }
        })

        vm.liveDataApps.observe(this,
            Observer {
                it?.let {
                    val gridCnt = vm.gridCnt
                    gvApps.numColumns = gridCnt
                    gvApps.adapter = ArrangeImageAdapter(
                        this,
                        Repo,
                        it.take(gridCnt * gridCnt).toMutableList(),
                        resources.getDimension(R.dimen.grid_view_size_in_arrange).toInt()/gridCnt, // 388 same to
                        vm.dir
                    ){
                        it.quickApp.toString().i(TAG)

                        if(vm.isMoving) {
                            when(it.quickApp.type) {
                                QuickAppType.EMPTY -> vm.moveApp(it.pos)
                                // with folder
                                else -> {
                                    AlertDialog.Builder(this).showDialog(
                                        message =
                                        if(it.quickApp.type == QuickAppType.FOLDER) "폴더 안에 넣으시겠습니까?"
                                        else "있는데 덮어 씌우겠습니까?",
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
                    false)
            }
        }

        vm.refreshAppGrid()

    }

    fun selectIcon(){
        CropImage.startPickImageActivity(this)
    }


    private fun openFolder(pickedApp: QuickApp) {

        if(pickedApp.dir.isEmpty()) {
            "폴더가 비어있습니다".toast()
            return
        }

        val afd = ArrangeFolderDialog(this,pickedApp.dir){
            vm.deleteCommonAppFromFolder(it)
        }
        afd.show()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(resultCode == Activity.RESULT_OK && requestCode == REQ_CODE_APP_SELECT) {
            data?.run {
                vm.saveCommonAppToCurPos(
                    CommonApp(
                        getStringExtra(PKG_NAME),
                        getStringExtra(LABEL),
                        false,
                        getBooleanExtra(IS_EXCEPT,false)
                    ))
            }
        } else if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageUri = CropImage.getPickImageResultUri(this, data)

            CropImage.activity(imageUri)
                .setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                .setAspectRatio(vm.myIconPixel, vm.myIconPixel)
                .start(this)

        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val result = CropImage.getActivityResult(data)
            val bitImg = MediaStore.Images.Media.getBitmap(this.contentResolver,result.uri)

            val cw = ContextWrapper(applicationContext)
            val dir = cw.getDir("imageDir", Context.MODE_PRIVATE)
            val myPath = File(dir, "${vm.dir}#${vm.curPos}")

            val fos = FileOutputStream(myPath)
            bitImg.compress(Bitmap.CompressFormat.PNG,100,fos)
//            bitImg.compress(Bitmap.CompressFormat.JPEG,100,fos)

            val b = BitmapFactory.decodeStream(FileInputStream(myPath))
            vm.saveImageCache("${vm.dir}#${vm.curPos}",b)

            vm.setHasImage(true)
        } else if (requestCode == REQ_CODE_EXPERT_SETTING && resultCode == Activity.RESULT_OK) {
            vm.saveExpertedQuickAppToCurPos()
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        val DIR = "DIR"
        val PKG_NAME = "PKG_NAME"
        val LABEL = "LABEL"
        val IS_EXCEPT = "IS_EXCEPT"
    }


}
