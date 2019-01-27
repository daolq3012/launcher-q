package seoft.co.kr.launcherq.ui.arrange

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_expert_setting.*
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.model.CustomComponentName
import seoft.co.kr.launcherq.data.model.CustomIntent
import seoft.co.kr.launcherq.data.model.Expert
import seoft.co.kr.launcherq.data.model.QuickAppType
import seoft.co.kr.launcherq.utill.SC
import seoft.co.kr.launcherq.utill.i
import seoft.co.kr.launcherq.utill.toast

class ExpertSettingActivity : AppCompatActivity() {

    val TAG = "ExpertSettingActivity#$#"

    var pos = 0
    var cmdType = 0

    val eos = ExpertOptionModels()
    lateinit var curCustomIntent : CustomIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expert_setting)

        pos = intent.getIntExtra(ES_POS,-1)
        cmdType = intent.getIntExtra(ES_TYPE,-1)

        initValues()

        initListener()
    }

    fun initValues(){


        if(cmdType == 1) { // edit

            SC.qApp4SetExpert?.expert?.let {
                if(pos == 0 ) curCustomIntent = it.useOne!!
                else {
                    if(it.useTwo != null) curCustomIntent = it.useTwo!![pos-1]!!
                }
            }

        } else { // add
            curCustomIntent = CustomIntent("")

        }











    }


    fun initListener(){

        tvAction.setOnClickListener { v ->
            ExpertOptionDialog(this,eos.actions) {
                it.i(TAG)
            }.apply { show() }
        }

        tvCategory.setOnClickListener { _ ->
            ExpertOptionDialog(this,eos.categorys) {
                it.i(TAG)
            }.apply { show() }
        }

        tvSetFlag.setOnClickListener { _ ->
            ExpertOptionDialog(this,eos.flags) {
                it.i(TAG)
            }.apply { show() }
        }

        tvAddFlag.setOnClickListener { _ ->
            ExpertOptionDialog(this,eos.flags) {
                it.i(TAG)
            }.apply { show() }
        }

        tvOk.setOnClickListener { _ ->

            if(etName.text.toString().isBlank()) {
                "name을 입력하세요".toast()
                return@setOnClickListener
            }

            curCustomIntent.apply {
                name = etName.text.toString()

                etUri.text.toString().let { uriData = if(it.isBlank()) null else it }
                etType.text.toString().let { type = if(it.isBlank()) null else it }
                etPkgName.text.toString().let { pkgName = if(it.isBlank()) null else it }

                val tmpCustomComponentName = CustomComponentName(etCnPkgName.text.toString(),etCnCls.text.toString())

                if(!(tmpCustomComponentName.compCls == "" && tmpCustomComponentName.compName == ""))
                    customComponentName = tmpCustomComponentName
            }




            if(SC.qApp4SetExpert!!.expert == null)
                SC.qApp4SetExpert!!.expert = Expert(null)

            with(SC.qApp4SetExpert!!.expert!!){
                if(pos == 0 ) useOne = curCustomIntent
                else {

                    if(useTwo == null)
                        useTwo = mutableListOf(null,null,null,null,null,null)

                    useTwo!![pos-1] = curCustomIntent
                }
            }

            SC.qApp4SetExpert!!.type = QuickAppType.EXPERT

            setResult(Activity.RESULT_OK)
            finish()
        }

        tvCancel.setOnClickListener { _ -> finish() }
    }


    companion object {
        val ES_POS = "ES_POS"
        val ES_TYPE = "ES_TYPE"

    }

}
