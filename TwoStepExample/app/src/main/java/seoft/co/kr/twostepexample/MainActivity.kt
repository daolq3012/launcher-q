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
            /**
             * provider test code
             */

            var insertCmd =  arrayOf(
                Command.toContentValues(Command(id = null, pkgName = "AA",cls = "AA",normalMessage = "CC",useEdit = false, editMessage = "C3C")),
                Command.toContentValues(Command(id = null, pkgName = "BB",cls = "BB",normalMessage = "CC",useEdit = false, editMessage = "C3C")),
                Command.toContentValues(Command(id = null, pkgName = "CC",cls = "CC",normalMessage = "CC",useEdit = false, editMessage = "C3C")),
                Command.toContentValues(Command(id = null, pkgName = "AA",cls = "AA",normalMessage = "CC",useEdit = false, editMessage = "C3C")),
                Command.toContentValues(Command(id = null, pkgName = "BB",cls = "BB",normalMessage = "CC",useEdit = false, editMessage = "C3C")),
                Command.toContentValues(Command(id = null, pkgName = "CC",cls = "CC",normalMessage = "CC",useEdit = false, editMessage = "C3C"))
            )

            contentResolver.bulkInsert(Command.URI_COMMAND ,insertCmd)

            "=============".i(TAG)

            val asdf = contentResolver.query(Command.getUriFromPkgName("BB"),null,null,null,null)
            Command.cursorToCommands(asdf).forEach { it.toString().i(TAG) }

            "=============".i(TAG)

            val kkk = contentResolver.query(Command.URI_COMMAND ,null,null,null,null)
            Command.cursorToCommands(kkk).forEach { it.toString().i(TAG) }

            "=============".i(TAG)

            contentResolver.delete(Command.getUriFromPkgName("BB"),null,null)

            val kkkk = contentResolver.query(Command.URI_COMMAND ,null,null,null,null)
            Command.cursorToCommands(kkkk).forEach { it.toString().i(TAG) }
            "=============".i(TAG)



        }

        bt2.setOnClickListener { _ ->
            val kkk = contentResolver.query(Command.URI_COMMAND ,null,null,null,null)

            Command.cursorToCommands(kkk).forEach{
                it.toString().i(TAG)
            }

        }


    }


}
