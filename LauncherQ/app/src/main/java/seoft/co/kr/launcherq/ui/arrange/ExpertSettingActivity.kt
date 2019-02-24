package seoft.co.kr.launcherq.ui.arrange

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_expert_setting.*
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.model.*
import seoft.co.kr.launcherq.utill.SC
import seoft.co.kr.launcherq.utill.toEditable
import seoft.co.kr.launcherq.utill.toast

/**
 * this activity is not mvvm structure
 */
class ExpertSettingActivity : AppCompatActivity() {

    val TAG = "ExpertSettingActivity#$#"

    var pos = 0
    var cmdType = 0

    val eos = ExpertOptionModels()
    lateinit var curCustomIntent : CustomIntent

    var eomAction = ExpertOption("ACTION_DEFAULT", eos.actionDefault)
    var eomCategorys = mutableListOf<ExpertOption>()
    var eomSetFlag = ExpertOption("SET DEFAULT",eos.flagDefault.toString())
    var eomAddFlag = mutableListOf<ExpertOption>()

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

            etName.text =  curCustomIntent.name.toEditable()
            etUri.text =  curCustomIntent.uriData?.toEditable() ?: "".toEditable()
            etType.text =  curCustomIntent.type?.toEditable() ?: "".toEditable()
            etPkgName.text =  curCustomIntent.pkgName?.toEditable() ?: "".toEditable()
            etCnPkgName.text =  curCustomIntent.customComponentName?.compName?.toEditable() ?: "".toEditable()
            etCnCls.text =  curCustomIntent.customComponentName?.compCls?.toEditable() ?: "".toEditable()

            with(eos.actions.find { it.result == curCustomIntent.action }){
                tvAction.text = this!!.name
                eomAction = this
            }

            with(eos.flags.find { it.result.toInt() == curCustomIntent.flag }){
                tvSetFlag.text = this!!.name
                eomSetFlag = this
            }


            curCustomIntent.categorys.forEach {
                val cur = it
                eos.categorys.find { it.result ==  cur }?.let { eomCategorys.add(it) }
            }
            tvCategory.text = eomCategorys.joinToString(postfix = "\n추가하기",separator = "\n"){it.name}


            curCustomIntent.addFlag?.let {
                it.forEach {
                    val cur = it
                    eos.flags.find { it.result.toInt() ==  cur }?.let { eomAddFlag.add(it) }
                }

                tvAddFlag.text = eomAddFlag.joinToString(postfix = "\n추가하기",separator = "\n"){it.name}
            }

        } else { // add
            curCustomIntent = CustomIntent("")
        }

    }


    fun initListener(){

        tvAction.setOnClickListener { v ->
            ExpertOptionDialog(this,eos.actions) {
                eomAction = it
                tvAction.text = /*if(eomAction.result == eos.actionDefault) "추가하기" else*/ eomAction.name
            }.apply { show() }
        }

        tvCategory.setOnClickListener { _ ->
            ExpertOptionDialog(this,eos.categorys) {

                if(it.result.isEmpty()) {
                    eomCategorys.clear()
                    tvCategory.text = "CATEGORY_DEFAULT\n추가하기"
                    return@ExpertOptionDialog
                }
                eomCategorys.add(it)

                tvCategory.text = eomCategorys.joinToString(postfix = "\n추가하기",separator = "\n"){it.name}

            }.apply { show() }
        }

        tvSetFlag.setOnClickListener { _ ->
            ExpertOptionDialog(this,eos.flags
                .toMutableList()
                .apply { add(0,ExpertOption("SET DEFAULT",eos.flagDefault.toString()) )}) {

                eomSetFlag = it
                tvSetFlag.text = if(eomSetFlag.result.toInt() == eos.flagDefault) "FLAG_ACTIVITY_NEW_TASK" else eomSetFlag.name
            }.apply { show() }
        }

        tvAddFlag.setOnClickListener { _ ->
            ExpertOptionDialog(this,eos.flags
                .toMutableList()
                    .apply { add(0,ExpertOption("CLEAR","") )}) {

                if(it.result.isEmpty()) {
                    eomAddFlag.clear()
                    tvAddFlag.text = "추가하기"
                    return@ExpertOptionDialog
                }
                eomAddFlag.add(it)

                tvAddFlag.text = eomAddFlag.joinToString(postfix = "\n추가하기",separator = "\n"){it.name}


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

                customComponentName = if(tmpCustomComponentName.compCls == "" && tmpCustomComponentName.compName == "") null
                else tmpCustomComponentName

                action = eomAction.result

                if(eomCategorys.isEmpty()) categorys = mutableListOf(eos.categoryDefault)
                else categorys = eomCategorys.map { it.result }.toMutableList()

                flag = eomSetFlag.result.toInt()

                if(eomAddFlag.isNotEmpty()) addFlag = eomAddFlag.map { it.result.toInt() }.toMutableList()
                else addFlag = null
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

//    좀더 테스트? 하고 삭제기능 ㄱㄱ

}
