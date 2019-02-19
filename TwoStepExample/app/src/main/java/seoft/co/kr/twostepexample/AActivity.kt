package seoft.co.kr.twostepexample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_a.*
import seoft.co.manage_two_step.Command

class AActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a)

        val getNormalMsg = intent.getStringExtra(Command.NORMAL_MESSAGE)
        val getEditMsg = intent.getStringExtra(Command.EDIT_MESSAGE)

        textView.text = "$getNormalMsg $getEditMsg"
        Toast.makeText(this,"$getNormalMsg $getEditMsg",Toast.LENGTH_LONG).show()

    }
}
