package seoft.co.kr.launcherq.ui.setting

import android.os.Bundle
import android.preference.PreferenceFragment
import android.support.v7.app.AppCompatActivity
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.utill.LaqSettingManager
import seoft.co.kr.launcherq.utill.SC
import seoft.co.kr.launcherq.utill.setupActionBar


class LauncherSettingActivity: AppCompatActivity() {

    val TAG = "LauncherSettingActivity#$#"

    companion object {
        lateinit var launcherSettingActivity : AppCompatActivity
    }

    private fun initToolbar(){
        setupActionBar(R.id.toolbar) {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher_setting)

        launcherSettingActivity = this

        initToolbar()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    class LauncherSettingFragment : PreferenceFragment() {

        val TAG = "LauncherSettingFragment"
        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)

            addPreferencesFromResource(R.xml.launcher_setting)

            initListener()

        }

        override fun onResume() {
            super.onResume()

        }

        fun initListener(){
            findPreference("clickGridCnt").setOnPreferenceClickListener { view ->
                val snd = SelectNumberDialog(activity!!, "그리드 카운트", "개",
                    Repo.preference.getGridCount(), 3,4){
                    Repo.preference.setGridCount(it)
                    SC.needResetUxSetting = true
                }
                snd.show()
                true
            }

            findPreference("clickViewSIze").setOnPreferenceClickListener { view ->
                val snd = SelectNumberDialog(activity!!, "뷰 사이즈","DP",
                    Repo.preference.getGridViewSize(),150,250){
                    Repo.preference.setGridViewSize(it)
                    SC.needResetUxSetting = true
                }
                snd.show()
                true
            }

            findPreference("clickDistance").setOnPreferenceClickListener { view ->
                val snd = SelectNumberDialog(activity!!, "원스탭 열기 거리","DP",
                    Repo.preference.getDistance(),100,200){
                    Repo.preference.setDistance(it)
                    SC.needResetUxSetting = true
                }
                snd.show()
                true
            }

            findPreference("clickInterval").setOnPreferenceClickListener { view ->
                val snd = SelectNumberDialog(activity!!, "투스탭 열기 유지 시간 간격","",
                    Repo.preference.getTwoStepOpenInterval(),4,24){
                    Repo.preference.setTwoStepOpenInterval(it)
                    SC.needResetUxSetting = true
                }
                snd.show()
                true
            }

            findPreference("clickBottomBoundary").setOnPreferenceClickListener { view ->
                val snd = SelectNumberDialog(activity!!, "하단 드래그 터치 영역","%",
                    Repo.preference.getBottomBoundary(),0,49){
                    Repo.preference.setBottomBoundary(it)
                    SC.needResetUxSetting = true
                }
                snd.show()
                true

            }

            findPreference("clickTopBoundary").setOnPreferenceClickListener { view ->
                val snd = SelectNumberDialog(activity!!, "상단 드래그 터치 영역","%",
                    Repo.preference.getTopBoundary(),0,49){
                    Repo.preference.setTopBoundary(it)
                    SC.needResetUxSetting = true
                }
                snd.show()
                true
            }

            findPreference("clickSaveSetting").setOnPreferenceClickListener { view ->


                val laqSettingJson = LaqSettingManager().getJson(Repo)

                // TODO save json


                true
            }

            findPreference("clickLoadSetting").setOnPreferenceClickListener { view ->


                // TODO load json & adjust this method
                val tmpString = "ABCD"
                LaqSettingManager().setLaqSetting(tmpString,Repo)

                true
            }
        }

    }


}
