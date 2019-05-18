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
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
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
    val REQ_CODE_APP_SELECT = 1000
    val REQ_CODE_APP_ICON_SELECT = 2000
    val REQ_CODE_EXPERT_SETTING = 3000

    lateinit var vm : ArrangeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_arrange)

        vm = ViewModelProviders.of(this,ArrangeViewModel(Repo).create()).get(ArrangeViewModel::class.java)
        binding.vm = vm
        binding.executePendingBindings()

        initToolbar()

        vm.observeActMsg(this, Observer {
            when(it) {

                // SHOW_OPTIONS has show drawer, etc... when select app activity
                MsgType.SELECT_APP -> {
                    val intent = Intent(applicationContext,SelectActivity::class.java).apply {
                        putExtra(SelectActivity.SHOW_ETC_OPTIONS,true)
                        putExtra(SelectActivity.SHOW_LABEL,true)
                    }
                    startActivityForResult(intent,REQ_CODE_APP_SELECT)
                }
                MsgType.SELECT_APP_IN_FOLDER -> {
                    val intent = Intent(applicationContext,SelectActivity::class.java).apply {
                        putExtra(SelectActivity.SHOW_ETC_OPTIONS,false)
                        putExtra(SelectActivity.SHOW_LABEL,true)
                    }
                    startActivityForResult(intent,REQ_CODE_APP_SELECT)
                }
                MsgType.OPEN_FOLDER -> openFolder(vm.pickedApp.value())
                MsgType.OPEN_ICON_SETTER ->{
                    if(vm.msg as Boolean) {
                        SelectorDialog(this,
                            R.string.select.TRANS(),
                            SelectorDialog.DialogSelectorInfo(R.string.edit.TRANS()),
                            SelectorDialog.DialogSelectorInfo(R.string.delete.TRANS()),
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
                                // with ic_folder_im
                                else -> {
                                    AlertDialog.Builder(this).showDialog(
                                        message =
                                        if(it.quickApp.type == QuickAppType.FOLDER) R.string.insert_to_folder.TRANS()
                                        else R.string.cover_this.TRANS(),
                                        postiveBtText = R.string.yes.TRANS(),
                                        negativeBtText = R.string.no.TRANS(),
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
        SelectorDialog(this,
            R.string.select.TRANS(),
            SelectorDialog.DialogSelectorInfo(R.string.use_default_icon.TRANS()),
            SelectorDialog.DialogSelectorInfo(R.string.bring_from_gallery.TRANS()),
            cb = {
                when(it) {
                    1 -> {
                        val intent = Intent(applicationContext,SelectActivity::class.java).apply {
                            putExtra(SelectActivity.SHOW_ETC_OPTIONS,false)
                            putExtra(SelectActivity.SHOW_LABEL,false)
                        }
                        startActivityForResult(intent,REQ_CODE_APP_ICON_SELECT)
                    }
                    2 -> {
                        CropImage.startPickImageActivity(this)
                    }
                }
            }
        ).create()
    }


    private fun openFolder(pickedApp: QuickApp) {

        if(pickedApp.dir.isEmpty()) {
            R.string.empty_folder.TRANS().toast()
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
        } else if (resultCode == Activity.RESULT_OK && requestCode == REQ_CODE_APP_ICON_SELECT){
            data?.run {

                val pkgName = getStringExtra(PKG_NAME)

                if(!Repo.imageCacheRepo.containsKey(pkgName))
                    Repo.imageCacheRepo.saveCache(pkgName,App.get.packageManager.getApplicationIcon(pkgName))

                val drawer = Repo.imageCacheRepo.getDrawable(pkgName)
                val bitImg = drawableToBitmap(drawer)

                adjustBitmap(bitImg)
            }
        }
        else if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageUri = CropImage.getPickImageResultUri(this, data)

            CropImage.activity(imageUri)
                .setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                .setAspectRatio(vm.myIconPixel, vm.myIconPixel)
                .start(this)

        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val result = CropImage.getActivityResult(data)
            val bitImg = MediaStore.Images.Media.getBitmap(this.contentResolver,result.uri)
            adjustBitmap(bitImg)

        } else if (requestCode == REQ_CODE_EXPERT_SETTING && resultCode == Activity.RESULT_OK) {
            vm.saveExpertedQuickAppToCurPos()
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    fun drawableToBitmap(drawable: Drawable): Bitmap {
        var bitmap: Bitmap? = null

        if (drawable is BitmapDrawable) {
            if (drawable.bitmap != null) {
                return drawable.bitmap!!
            }
        }

        if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
            bitmap = Bitmap.createBitmap(
                1,
                1,
                Bitmap.Config.ARGB_8888
            ) // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        }

        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
        drawable.draw(canvas)
        return bitmap!!
    }

    fun adjustBitmap(bitmap: Bitmap){
        val cw = ContextWrapper(applicationContext)
        val dir = cw.getDir("imageDir", Context.MODE_PRIVATE)
        val myPath = File(dir, "${vm.dir}#${vm.curPos}")
        val fos = FileOutputStream(myPath)
        bitmap.compress(Bitmap.CompressFormat.PNG,50,fos)
        val b = BitmapFactory.decodeStream(FileInputStream(myPath))
        vm.saveImageCache("${vm.dir}#${vm.curPos}",b)

        vm.setHasImage(true)
    }

    private fun initToolbar(){
        setupActionBar(R.id.toolbar) {
            setHomeAsUpIndicator(R.drawable.ic_back)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setDisplayShowTitleEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        val DIR = "DIR"
        val PKG_NAME = "PKG_NAME"
        val LABEL = "LABEL"
        val IS_EXCEPT = "IS_EXCEPT"
    }


}
