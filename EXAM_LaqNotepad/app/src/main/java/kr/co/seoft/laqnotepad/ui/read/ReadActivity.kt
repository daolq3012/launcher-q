package kr.co.seoft.laqnotepad.ui.read

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_read.*
import kr.co.seoft.laqnotepad.R
import kr.co.seoft.laqnotepad.ui.edit.EditActivity
import kr.co.seoft.laqnotepad.ui.main.MainActivity
import kr.co.seoft.laqnotepad.util.EK_CONTENT
import kr.co.seoft.laqnotepad.util.EK_ID
import kr.co.seoft.laqnotepad.util.EK_IS_REMOVE
import kr.co.seoft.laqnotepad.util.TO_STRING

class ReadActivity : AppCompatActivity() {

    lateinit var content:String
    var id:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read)

        content = intent.getStringExtra(EK_CONTENT)
        id = intent.getIntExtra(EK_ID,0)

        tvContent.text = content

        initListener()

    }

    private fun initListener(){

        tvEdit.setOnClickListener { _ ->
            startActivityForResult(Intent(applicationContext,EditActivity::class.java).apply {
                putExtra(EK_ID,id)
                putExtra(EK_CONTENT,content)
            }, MainActivity.EDIT_NOTE)
        }

        tvHome.setOnClickListener { _ ->
            finish()
        }

        tvDelete.setOnClickListener { _ ->

            AlertDialog
                .Builder(this)
                .setMessage(R.string.sure_delete.TO_STRING())
                .setPositiveButton(R.string.yes.TO_STRING()) {d, w ->
                    setResult(Activity.RESULT_OK, Intent().apply {
                        putExtra(EK_ID,id)
                        putExtra(EK_IS_REMOVE,true)
                    })
                    finish()
                }
                .setNegativeButton(R.string.no.TO_STRING()) {d, w ->
                }
                .show()

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && data !=null) {
            when (requestCode) {
                MainActivity.EDIT_NOTE ->{
                    setResult(Activity.RESULT_OK, data)
                    finish()
                }
            }
        }


    }




}
