package seoft.co.kr.launcherq.ui.setting

import android.os.Bundle
import android.preference.PreferenceFragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.utill.setupActionBar


class BgScreenSettingActivity: AppCompatActivity() {

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

            val keywordScreen = findPreference("key1")

            keywordScreen.setOnPreferenceClickListener { view ->
                Log.i(TAG,"key1")
                true
            }

            findPreference("key2").setOnPreferenceClickListener { v ->
                true
            }

        }
    }

}
