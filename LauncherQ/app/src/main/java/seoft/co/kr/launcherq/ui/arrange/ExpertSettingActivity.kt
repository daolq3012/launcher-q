package seoft.co.kr.launcherq.ui.arrange

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_expert_setting.*
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.model.*
import seoft.co.kr.launcherq.utill.*

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
        initToolbar()
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
                tvAction.text = eomAction.name
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

//        tvOk.setOnClickListener { _ ->
//
//            if(etName.text.toString().isBlank()) {
//                "name을 입력하세요".toast()
//                return@setOnClickListener
//            }
//
//            curCustomIntent.apply {
//                name = etName.text.toString()
//
//                etUri.text.toString().let { uriData = if(it.isBlank()) null else it }
//                etType.text.toString().let { type = if(it.isBlank()) null else it }
//                etPkgName.text.toString().let { pkgName = if(it.isBlank()) null else it }
//
//                val tmpCustomComponentName = CustomComponentName(etCnPkgName.text.toString(),etCnCls.text.toString())
//
//                customComponentName = if(tmpCustomComponentName.compCls == "" && tmpCustomComponentName.compName == "") null
//                else tmpCustomComponentName
//
//                action = eomAction.result
//
//                if(eomCategorys.isEmpty()) categorys = mutableListOf(eos.categoryDefault)
//                else categorys = eomCategorys.map { it.result }.toMutableList()
//
//                flag = eomSetFlag.result.toInt()
//
//                if(eomAddFlag.isNotEmpty()) addFlag = eomAddFlag.map { it.result.toInt() }.toMutableList()
//                else addFlag = null
//            }
//
//            if(SC.qApp4SetExpert!!.expert == null)
//                SC.qApp4SetExpert!!.expert = Expert(null)
//
//            with(SC.qApp4SetExpert!!.expert!!){
//                if(pos == 0 ) useOne = curCustomIntent
//                else {
//
//                    if(useTwo == null)
//                        useTwo = mutableListOf(null,null,null,null,null,null)
//
//                    useTwo!![pos-1] = curCustomIntent
//                }
//            }
//
//            SC.qApp4SetExpert!!.type = QuickAppType.EXPERT
//
//            setResult(Activity.RESULT_OK)
//            finish()
//        }
//
//        tvCancel.setOnClickListener { _ -> finish() }
//
//        tvSpeed.setOnClickListener { _ ->
//            SelectorDialog(this,
//                "선택하세요",
//                SelectorDialog.DialogSelectorInfo("웹페이지 빠른 등록"),
//                SelectorDialog.DialogSelectorInfo("전화 빠른 등록"),
//                SelectorDialog.DialogSelectorInfo("앱 실행 빠른 등록"),
//                cb = {
//                    when(it) {
//                        1 -> setWeb()
//                        2 -> setPhone()
//                        3 -> setApp()
//                    }
//                }
//            ).create()
//        }

    }

    fun done(){
        if(etName.text.toString().isBlank()) {
            "name을 입력하세요".toast()
            return
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

    fun setWeb(){
        AlertDialog.Builder(this).showDialogWithTwoInput(
            message = "(1)표시 될 이름과 (2)웹 페이지 주소를 입력하세요",
            postiveBtText = "확인",
            negativeBtText = "취소",
            cbPostive = {
                etName.setText(it.split(SC.SPLITTER)[0])
                etUri.setText(it.split(SC.SPLITTER)[1])
                eomAction = ExpertOption("ACTION_VIEW", "android.intent.action.VIEW")
                tvAction.text = eomAction.name
            },
            cbNegative = {},
            inputType1 = InputType.TYPE_CLASS_TEXT,
            text1 = etName.text.toString(),
            hint1 = "Google",
            inputType2 = InputType.TYPE_CLASS_TEXT,
            text2 = etUri.text.toString(),
            hint2 = "http://google.com"
        )
    }

    fun setPhone(){
        AlertDialog.Builder(this).showDialogWithTwoInput(
            message = "(1)표시 될 이름과 (2)전화번호를 입력하세요",
            postiveBtText = "확인",
            negativeBtText = "취소",
            cbPostive = {
                etName.setText(it.split(SC.SPLITTER)[0])
                etUri.setText("tel:${it.split(SC.SPLITTER)[1]}")
                eomAction = ExpertOption("ACTION_DIAL", "android.intent.action.DIAL")
                tvAction.text = eomAction.name
            },
            cbNegative = {},
            inputType1 = InputType.TYPE_CLASS_TEXT,
            text1 = etName.text.toString(),
            hint1 = "Mike",
            inputType2 = InputType.TYPE_CLASS_PHONE,
            text2 = etUri.text.toString(),
            hint2 = "01023459876"
        )
    }

    fun setApp(){
        AlertDialog.Builder(this).showDialogWithTwoInput(
            message = "(1)표시 될 이름과 (2)패키지명를 입력하세요",
            postiveBtText = "확인",
            negativeBtText = "취소",
            cbPostive = {
                it.split(SC.SPLITTER).i(TAG)
                etName.setText(it.split(SC.SPLITTER)[0])
                etPkgName.setText(it.split(SC.SPLITTER)[1])
            },
            cbNegative = {},
            inputType1 = InputType.TYPE_CLASS_TEXT,
            text1 = etName.text.toString(),
            hint1 = "(1)Google Map",
            inputType2 = InputType.TYPE_CLASS_TEXT,
            text2 = etPkgName.text.toString(),
            hint2 = "(2)com.google.android.apps.maps"
        )
    }


    private fun initToolbar(){
        setupActionBar(R.id.toolbar) {
            setHomeAsUpIndicator(R.drawable.ic_back)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setDisplayShowTitleEnabled(true)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mn_expert_setting,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        item?: return true

        when(item.itemId) {
            R.id.menu_es_more -> {
                SelectorDialog(this,
                    "선택하세요",
                    SelectorDialog.DialogSelectorInfo("웹페이지 빠른 등록"),
                    SelectorDialog.DialogSelectorInfo("전화 빠른 등록"),
                    SelectorDialog.DialogSelectorInfo("앱 실행 빠른 등록"),
                    cb = {
                        when(it) {
                            1 -> setWeb()
                            2 -> setPhone()
                            3 -> setApp()
                        }
                    }
                ).create()
            }
            R.id.menu_es_done -> done()
            android.R.id.home -> finish()
        }

        return true
    }
    companion object {
        val ES_POS = "ES_POS"
        val ES_TYPE = "ES_TYPE"

    }

}
