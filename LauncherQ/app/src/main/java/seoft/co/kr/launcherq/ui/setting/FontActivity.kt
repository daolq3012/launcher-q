package seoft.co.kr.launcherq.ui.setting

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_font.*
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.model.WidgetInfoType
import seoft.co.kr.launcherq.utill.*


class FontActivity : AppCompatActivity() {

    val TAG = "FontActivity#$#"

    companion object {
        const val FONT = "FONT"
        const val WIDGET_TYPE = "WIDGET_TYPE"
        const val DEFAULT_FONT = "DEFAULT_FONT"
        const val LOAD_FONT_FILE = "LOAD_FONT_FILE"

    }

    val REQ_LOAD = 1112

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

    lateinit var widgetType : WidgetInfoType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_font)

        lvFont.adapter = FontListAdapter()

        initToolbar()

        widgetType = intent.getStringExtra(WIDGET_TYPE).getWidget()
    }

    fun selectFont(fontFileName:String){

        fontFileName.i(TAG)

        if(fontFileName == LOAD_FONT_FILE) {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply { type = "application/*" }
            startActivityForResult(intent, LauncherSettingActivity.REQ_LOAD)
        } else {
            val rstIntent = Intent()
                .apply {
                    putExtra(FONT,fontFileName)
                }
            setResult(Activity.RESULT_OK,rstIntent)
            finish()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LauncherSettingActivity.REQ_LOAD && resultCode == Activity.RESULT_OK) {
            try{
                if(data == null || data.data == null ) throw IllegalArgumentException("FILE_LOAD_ERR")

                val ois = contentResolver.openInputStream(data.data)
                ois?:throw IllegalArgumentException("FILE_LOAD_ERR")
                val ofo = openFileOutput("${widgetType.getStr}.ttf", Context.MODE_PRIVATE)
                ofo?: throw IllegalArgumentException("FILE_SAVE_ERR")
                ofo.write(ois.readBytes())
                ofo.close()
                ois.close()

                val rstIntent = Intent()
                    .apply {
                        putExtra(FONT,LOAD_FONT_FILE)
                    }
                setResult(Activity.RESULT_OK,rstIntent)
                finish()

            } catch (e:Exception) {
                e.printStackTrace()
                "파일을 불러올 수 없습니다".toast()
            }

            "설정 불러오기 완료".toast()
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
            else if(fonts[pos] == DEFAULT_FONT) "스마트폰 기본 폰트"
            else fonts[pos]

            tvFont.typeface = if(fonts[pos] == LOAD_FONT_FILE) Typeface.DEFAULT
            else if(fonts[pos] == DEFAULT_FONT) Typeface.DEFAULT
            else Typeface.createFromAsset(App.get.assets, fonts[pos] )

            view.setOnClickListener { v ->
                selectFont(fonts[pos])
            }
            return view
        }

    }


}