package seoft.co.kr.launcherq.ui.setting

import android.app.Activity
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.preference.PreferenceFragment
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.theartofdev.edmodo.cropper.CropImage
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.utill.SC
import seoft.co.kr.launcherq.utill.i
import seoft.co.kr.launcherq.utill.setupActionBar


class BgScreenSettingActivity: AppCompatActivity() {

    val TAG = "BgScreenSettingActivity#$#"

    private fun initToolbar(){
        setupActionBar(R.id.toolbar) {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bg_screen_setting)

        initToolbar()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    class BgScreenSettingFragment : PreferenceFragment() {

        val TAG = "SettingFragment"

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)

            addPreferencesFromResource(R.xml.bg_screen_setting)

            initListener()

        }

        fun initListener(){
            findPreference("simpleColor").setOnPreferenceClickListener { view ->

                true
            }

            findPreference("specificImage").setOnPreferenceClickListener { view ->
                CropImage.startPickImageActivity(this.activity)
                true
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val deviceWidth = Repo.preference.getDeviceX()
        val deviceHeight = Repo.preference.getDeviceY()

        deviceWidth.toString().i(TAG)
        deviceHeight.toString().i(TAG)

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
        }
    }

}
