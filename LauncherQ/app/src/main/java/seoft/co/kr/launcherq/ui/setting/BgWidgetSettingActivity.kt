package seoft.co.kr.launcherq.ui.setting

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.RelativeLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_bg_widget_setting.*
import me.priyesh.chroma.ChromaDialog
import me.priyesh.chroma.ColorMode
import me.priyesh.chroma.ColorSelectListener
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.data.model.WidgetInfoType
import seoft.co.kr.launcherq.databinding.ActivityBgWidgetSettingBinding
import seoft.co.kr.launcherq.ui.MsgType
import seoft.co.kr.launcherq.utill.*

class BgWidgetSettingActivity : AppCompatActivity() {

    val TAG = "BgWidgetSettingActivity#$#"

    companion object {
        val WIDGET_TYPE = "WIDGET_TYPE"
    }

    var xx = 0
    var yy = 0
    var saveX = 10
    var saveY = 10

    lateinit var vm : BgWidgetSettingViewModel

    private lateinit var binding: ActivityBgWidgetSettingBinding

    lateinit var widgetType : WidgetInfoType

    val REQ_FONT = 900

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_bg_widget_setting)

        widgetType = intent.getStringExtra(WIDGET_TYPE).getWidget()

        vm = ViewModelProviders.of(this, BgWidgetSettingViewModel(Repo, widgetType).create()).get(BgWidgetSettingViewModel::class.java)
        binding.vm = vm
        binding.executePendingBindings()

        vm.observeActMsg(this, Observer {
            when(it) {
                MsgType.PICK_COLOR -> pickColor(vm.msg as String)
                MsgType.OPEN_INPUT_DIALOG -> openInputDialog(vm.msg as String)
                MsgType.START_ACTIVITY -> {
                    if(vm.msg as Int == vm.FONT_ACTIVITY) {

                        val fontIntent = Intent(application,FontActivity::class.java).apply {
                            putExtra(FontActivity.WIDGET_TYPE, widgetType.getStr)
                        }

                        startActivityForResult(fontIntent,REQ_FONT)
                    }

                }
            }
        })

        initListener(widgetType)

        vm.start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQ_FONT && resultCode == Activity.RESULT_OK) {

            data?.run {
                vm.saveFont(getStringExtra(FontActivity.FONT))
            }

        }


    }

    fun initListener(widgetType : WidgetInfoType){

        val textViews = arrayListOf(
            R.id.tvTime,
            R.id.tvAmpm,
            R.id.tvDate,
            R.id.tvDow,
            R.id.tvText
        )

        val tv = findViewById<TextView>(textViews[widgetType.getInt])
        tv.setOnTouchListener(ChoiceTouchListener())
    }

    inner class ChoiceTouchListener : View.OnTouchListener {
        override fun onTouch(view: View?, event: MotionEvent?): Boolean {

            event?.let {
                view?.let {

                    val X = event.rawX.toInt()
                    val Y = event.rawY.toInt()
                    when (event.getAction() and MotionEvent.ACTION_MASK) {
                        MotionEvent.ACTION_DOWN -> {
                            val lParams = view.layoutParams as RelativeLayout.LayoutParams
                            xx = X - lParams.leftMargin
                            yy = Y - lParams.topMargin
                        }
                        MotionEvent.ACTION_MOVE -> {
                            val layoutParams = view.layoutParams as RelativeLayout.LayoutParams

                            layoutParams.leftMargin = X - xx
                            layoutParams.topMargin = Y - yy


                            view.layoutParams = layoutParams
                        }
                        MotionEvent.ACTION_UP -> {
                            "${saveX} $saveY".i("ACTION_UP")
                            saveX = X - xx
                            saveY = Y - yy
                            vm.savePos(saveX,saveY)
                        }
                    }
                    rlRoot.invalidate()
                }
                return true
            }
            return true
        }
    }

    fun pickColor(pickedColor:String) {

        ChromaDialog.Builder()
            .initialColor(pickedColor.toIntColor())
            .colorMode(ColorMode.RGB) // There's also ARGB and HSV
            .onColorSelected(object : ColorSelectListener {
                override fun onColorSelected(color: Int) {
                    vm.saveColor(color.toStrColor())
                }
            })
            .create()
            .show(supportFragmentManager, "ChromaDialog")

    }

    fun openInputDialog(befStr:String){

        val bgWidgetContent = BgWidgetContent()
        AlertDialog.Builder(this).showDialogWithInput(
            title = bgWidgetContent.widgetClassContents[widgetType.getInt].title,
            message = bgWidgetContent.widgetClassContents[widgetType.getInt].explain,
            postiveBtText = "확인",
            negativeBtText = "취소",
            cbPostive = {
                vm.saveEtc(it)
            },
            cbNegative = {

            },
            inputType = InputType.TYPE_CLASS_TEXT,
            text = befStr
        )
    }

    override fun onPause() {
        super.onPause()

        vm.saveWhenOnPause()
    }

}
