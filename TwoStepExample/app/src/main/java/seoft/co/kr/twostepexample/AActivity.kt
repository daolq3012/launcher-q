package seoft.co.kr.twostepexample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_a.*

class AActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a)

        val getValue = intent.getStringExtra("LQ_DATA")

        textView.text = getValue
        Toast.makeText(this,getValue,Toast.LENGTH_LONG).show()

    }
}
