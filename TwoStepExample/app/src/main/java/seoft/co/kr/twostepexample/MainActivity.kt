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
        val lqProvider = LQProvider(this)


        bt.setOnClickListener { _ ->

            with(lqProvider){
                remove("seoft.co.kr.twostepexample")

                insert("seoft.co.kr.twostepexample", "seoft.co.kr.twostepexample.AActivity#$#THIS_IS_A")
                insert("seoft.co.kr.twostepexample", "seoft.co.kr.twostepexample.BActivity#$#THIS_IS_B")
                null
            }

        }

        bt2.setOnClickListener { _ ->
            lqProvider.select("seoft.co.kr.twostepexample").forEach{
                Log.i(TAG,it.toString())
            }

            Log.i(TAG,"#############")

            lqProvider.select("seoft.co.kr.twostepexample","seoft.co.kr.twostepexample.AActivity#$#THIS_IS_A").forEach{
                Log.i(TAG,it.toString())
            }

        }


    }


}
