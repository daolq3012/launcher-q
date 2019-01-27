package seoft.co.kr.launcherq.ui.arrange

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_expert_setting.*
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.utill.i

class ExpertSettingActivity : AppCompatActivity() {

    val TAG = "ExpertSettingActivity#$#"

    var pos = 0
    var cmdType = 0

    val eos = ExpertOptionModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expert_setting)

        pos = intent.getIntExtra(ES_POS,-1)
        cmdType = intent.getIntExtra(ES_TYPE,-1)


        initListener()

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


    }


    companion object {
        val ES_POS = "ES_POS"
        val ES_TYPE = "ES_TYPE"

    }

}
