package kr.co.seoft.laqnotepad.ui.edit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_edit.*
import kr.co.seoft.laqnotepad.R
import kr.co.seoft.laqnotepad.util.EK_CONTENT
import kr.co.seoft.laqnotepad.util.EK_ID
import kr.co.seoft.laqnotepad.util.toEditable

class EditActivity : AppCompatActivity() {

    lateinit var content:String
    var id:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        id = intent.getIntExtra(EK_ID,NEW_NOTE)

        if(id == NEW_NOTE) {

        } else {
            content = intent.getStringExtra(EK_CONTENT)
            etContent.text = content.toEditable()
        }

        initListener()

    }

    private fun initListener(){

        etContent.requestFocus()

        tvSave.setOnClickListener { _ ->

            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(EK_CONTENT,etContent.text.toString())
                putExtra(EK_ID,id)
            })
            finish()
        }

        tvCancel.setOnClickListener { _ ->
            finish()
        }




    }


    companion object {
        const val NEW_NOTE = -1
    }

}
