package seoft.co.kr.twostepexample

import android.content.ContentValues
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import seoft.co.manage_two_step.Command
import seoft.co.manage_two_step.CommandRepo

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity#$#"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bt.setOnClickListener { _ ->
            val insertCmd =  listOf(
                Command(id = null, title = "AActivity", pkgName = "seoft.co.kr.twostepexample",cls = "seoft.co.kr.twostepexample.AActivity",normalMessage = "AA",useEdit = true),
                Command(id = null, title = "BActivity with param", pkgName = "seoft.co.kr.twostepexample",cls = "seoft.co.kr.twostepexample.BActivity",normalMessage = "BB",useEdit = false),
                Command(id = null, title = "AActivity", pkgName = "seoft.co.kr.twostepexample",cls = "seoft.co.kr.twostepexample.AActivity",normalMessage = "AA",useEdit = true),
                Command(id = null, title = "BActivity with param", pkgName = "seoft.co.kr.twostepexample",cls = "seoft.co.kr.twostepexample.BActivity",normalMessage = "BB",useEdit = false),
                Command(id = null, title = "AActivity", pkgName = "seoft.co.kr.twostepexample",cls = "seoft.co.kr.twostepexample.AActivity",normalMessage = "AA",useEdit = true),
                Command(id = null, title = "BActivity with param", pkgName = "seoft.co.kr.twostepexample",cls = "seoft.co.kr.twostepexample.BActivity",normalMessage = "BB",useEdit = false),
                Command(id = null, title = "AActivity", pkgName = "seoft.co.kr.twostepexample",cls = "seoft.co.kr.twostepexample.AActivity",normalMessage = "AA",useEdit = true),
                Command(id = null, title = "BActivity with param", pkgName = "seoft.co.kr.twostepexample",cls = "seoft.co.kr.twostepexample.BActivity",normalMessage = "BB",useEdit = false)
            )
            CommandRepo.insertCommands(this,insertCmd)
            "done".toast()
        }

        bt2.setOnClickListener { _ ->
            CommandRepo.deleteFromPkgName(this,"seoft.co.kr.twostepexample")
            "done".toast()
        }



    }


}
