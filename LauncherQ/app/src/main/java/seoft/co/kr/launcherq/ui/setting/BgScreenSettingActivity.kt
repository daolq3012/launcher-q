package seoft.co.kr.launcherq.ui.setting

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.SwitchPreference
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.theartofdev.edmodo.cropper.CropImage
import me.priyesh.chroma.ChromaDialog
import me.priyesh.chroma.ColorMode
import me.priyesh.chroma.ColorSelectListener
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.ui.setting.BgWidgetSettingActivity.Companion.WIDGET_TYPE
import seoft.co.kr.launcherq.utill.*


class BgScreenSettingActivity: AppCompatActivity() {

    val TAG = "BgScreenSettingActivity#$#"

    companion object {
        lateinit var bgScreenSettingActivity : AppCompatActivity
    }

    private fun initToolbar(){
        setupActionBar(R.id.toolbar) {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bg_screen_setting)

        bgScreenSettingActivity = this

        initToolbar()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    class BgScreenSettingFragment : PreferenceFragment() {

        val TAG = "SettingFragment"
        lateinit var widgetPreferencesScreens : Array<Preference>
        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)

            addPreferencesFromResource(R.xml.bg_screen_setting)

            initListener()

        }

        override fun onResume() {
            super.onResume()
            widgetPreferencesScreens[1].isEnabled = Repo.preference.getBgTimeUse()
            widgetPreferencesScreens[3].isEnabled = Repo.preference.getBgDateUse()

        }

        fun initListener(){
            findPreference("simpleColor").setOnPreferenceClickListener { view ->
                openColorSetting()
                true
            }

            findPreference("specificImage").setOnPreferenceClickListener { view ->

                CropImage.startPickImageActivity(this.activity)
                true
            }

            widgetPreferencesScreens = arrayOf(
                findPreference("timeWidget"),
                findPreference("ampmWidget"),
                findPreference("dateWidget"),
                findPreference("dowWidget"),
                findPreference("textWidget")
            )

            for ( wps in widgetPreferencesScreens) {
                wps.setOnPreferenceClickListener {
                    Intent(context,BgWidgetSettingActivity::class.java)
                        .apply { putExtra(WIDGET_TYPE,wps.key)
                        startActivity(this)}
                    true
                }
            }



        }


        fun openColorSetting(){

            val deviceWidth = Repo.preference.getDeviceX()
            val deviceHeight = Repo.preference.getDeviceY()

            val pickedColor = Repo.preference.getBgImageColor()

            ChromaDialog.Builder()
                .initialColor(pickedColor.toIntColor())
                .colorMode(ColorMode.RGB) // There's also ARGB and HSV
                .onColorSelected(object : ColorSelectListener {
                    override fun onColorSelected(color: Int) {
                        val bit = Bitmap.createBitmap(deviceWidth,deviceHeight, Bitmap.Config.ARGB_8888)
                        bit.eraseColor(color)
                        Repo.backgroundRepo.saveBitmap(bit, App.get)
                        "배경화면 설정 완료".toast()

                        val strPickColor = String.format("#%08X", color)
                        Repo.preference.setBgImageColor(strPickColor)
                        SC.needResetSetting = true

                    }
                })
                .create()
                .show(bgScreenSettingActivity.supportFragmentManager, "ChromaDialog")
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val deviceWidth = Repo.preference.getDeviceX()
        val deviceHeight = Repo.preference.getDeviceY()

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageUri = CropImage.getPickImageResultUri(this, data)
            CropImage.activity(imageUri)
                .setAspectRatio(deviceWidth, deviceHeight)
                .start(this)
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val result = CropImage.getActivityResult(data)

            val bitImg = MediaStore.Images.Media.getBitmap(this.contentResolver,result.uri)

            Repo.backgroundRepo.saveBitmap(bitImg,this)
            SC.needResetSetting = true
            "배경화면 설정 완료".toast()
        }
    }
}
