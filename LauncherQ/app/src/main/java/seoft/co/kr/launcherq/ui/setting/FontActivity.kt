package seoft.co.kr.launcherq.ui.setting

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_font.*
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.utill.i
import seoft.co.kr.launcherq.utill.setupActionBar

class FontActivty : AppCompatActivity() {

    val TAG = "FontActivty#$#"

    companion object {
        val FONT = "FONT"
        val DEFAULT_FONT = "DEFAULT_FONT"
        var LOAD_FONT_FILE = "LOAD_FONT_FILE"

    }


    val fonts = arrayListOf(
        "fonts/lato_bold.ttf",
        "fonts/lato_light.ttf",
        "fonts/lato_regular.ttf",
        "fonts/opensans_bold.ttf",
        "fonts/opensans_light.ttf",
        "fonts/opensans_regular.ttf",
        "fonts/roboto_bold.ttf",
        "fonts/roboto_light.ttf",
        "fonts/roboto_medium.ttf",
        "fonts/roboto_regular.ttf",
        "fonts/roboto_thin.ttf",
        LOAD_FONT_FILE,
        DEFAULT_FONT
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_font)

        lvFont.adapter = FontListAdapter()

        initToolbar()
    }

    fun selectFont(fontFileName:String){

        fontFileName.i(TAG)

        if(fontFileName == LOAD_FONT_FILE) {

        } else {
            val rstIntent = Intent()
                .apply {
                    putExtra(FONT,fontFileName)
                }
            setResult(Activity.RESULT_OK,rstIntent)
            finish()
        }
    }


    private fun initToolbar(){
        setupActionBar(R.id.toolbar) {
            setHomeAsUpIndicator(R.drawable.ic_arrow_back_white)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    inner class FontListAdapter : BaseAdapter(){
        override fun getItem(pos: Int): Any {
            return fonts[pos]
        }

        override fun getItemId(id: Int): Long {
            return 0
        }

        override fun getCount(): Int {
            return fonts.size
        }

        override fun getView(pos: Int, convertView: View?, parent: ViewGroup?): View {
            val view = LayoutInflater.from(baseContext).inflate(R.layout.item_font,null)
            val tvFont = view.findViewById<TextView>(R.id.tvFont)

            tvFont.text = if(fonts[pos] == LOAD_FONT_FILE) "사용자 폰트 설정"
            else if(fonts[pos] == DEFAULT_FONT) "디바이스 폰트"
            else fonts[pos]

            view.setOnClickListener { v ->
                selectFont(fonts[pos])
            }
            return view
        }

    }


}