package seoft.co.kr.twostepexample

import android.content.ContentValues
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity#$#"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        bt.setOnClickListener { _ ->
            var insertCmd = Command.toContentValues(
                Command(id = null, pkgName = "AA",cls = "AA",normalMessage = "AA",useEdit = false, editMessage = "AA")
            )

            var kk = contentResolver.insert(Command.URI_COMMAND ,insertCmd)
            kk.toString().i(TAG)

            insertCmd = Command.toContentValues(
                Command(id = null, pkgName = "BB",cls = "BB",normalMessage = "BB",useEdit = false, editMessage = "BB")
            )

            kk = contentResolver.insert(Command.URI_COMMAND ,insertCmd)
            kk.toString().i(TAG)

            insertCmd = Command.toContentValues(
                Command(id = null, pkgName = "CC",cls = "CC",normalMessage = "CC",useEdit = false, editMessage = "CC")
            )

            kk = contentResolver.insert(Command.URI_COMMAND ,insertCmd)
            kk.toString().i(TAG)



        }

        bt2.setOnClickListener { _ ->
            val kkk = contentResolver.query(Command.URI_COMMAND ,null,null,null,null)

            Command.cursorToCommands(kkk).forEach{
                it.toString().i(TAG)
            }

        }


    }


}
