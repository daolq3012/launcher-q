package seoft.co.kr.launcherq.ui.setting

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceFragment
import android.support.v4.provider.DocumentFile
import android.support.v7.app.AppCompatActivity
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.ui.opensource.OpensourceActivity
import seoft.co.kr.launcherq.utill.*
import java.util.*


class LauncherSettingActivity: AppCompatActivity() {

    val TAG = "LauncherSettingActivity#$#"


    companion object {
        const val REQ_SAVE = 1111
        const val REQ_LOAD = 1112

        // using save data
        var strData = ""

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
                    Repo.preference.getDistance(),80,180){
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
                (activity as LauncherSettingActivity).saveData(laqSettingJson)
                true
            }

            findPreference("clickLoadSetting").setOnPreferenceClickListener { view ->
                (activity as LauncherSettingActivity).loadData()
                true
            }


            findPreference("clickMyIconPixelSetting").setOnPreferenceClickListener { view ->
                val snd = SelectNumberDialog(activity!!, "임의 아이콘 크기 설정","pixel",
                    Repo.preference.getTopBoundary(),10,100){
                    Repo.preference.setTopBoundary(it)
                }
                snd.show()
                "해당 사이즈를 적용하려면 임의 아이콘을 재 설정해야합니다".toast()
                true
            }

            findPreference("clickShareApp").setOnPreferenceClickListener { view ->
                val intent = Intent(Intent.ACTION_SEND) .apply { type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=seoft.co.kr.launcherq") }

                val chooser = Intent.createChooser(intent, "투표투표")
                    .apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                App.get.startActivity(chooser)
                true
            }

            findPreference("clickOpensource").setOnPreferenceClickListener { view ->
                startActivity(Intent(activity.applicationContext, OpensourceActivity::class.java))
                true
            }

            findPreference("clickLauncherQInfo").setOnPreferenceClickListener { view ->
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://seoft.co.kr/laq")))
                true
            }

            findPreference("clickSeoft").setOnPreferenceClickListener { view ->
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://seoft.co.kr")))
                true
            }



        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)

            "ABCD".i(TAG)

            if(requestCode == REQ_LOAD) {
                data!!.data.path.i(TAG)
            }

        }

    }

    fun saveData(jsonData:String) {
        strData = jsonData
        "저장할 디렉토리를 선택하세요".toast()
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply { addCategory(Intent.CATEGORY_DEFAULT) }
        startActivityForResult(Intent.createChooser(intent, "choose saving directory"), REQ_SAVE)
    }

    fun loadData(){
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply { type = "text/plain" }
        startActivityForResult(intent, REQ_LOAD)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQ_SAVE && resultCode == Activity.RESULT_OK) {
            try{
                if(data == null || data.data == null ) throw IllegalArgumentException("FILE_SAVE_ERR")

                val newFile = DocumentFile.fromTreeUri(this, data.data)?.createFile("text/plain","laq_setting_bak_${Date().time}")
                newFile?: throw IllegalArgumentException("FILE_SAVE_ERR")

                val os = contentResolver.openOutputStream(newFile.uri)
                os?: throw IllegalArgumentException("FILE_SAVE_ERR")
                os.write(strData.toByteArray())
                os.close()
            } catch (e:Exception) {
                e.printStackTrace()
                "파일을 저장할 수 없습니다".toast()
            }

            "설정 저장 완료".toast()

        } else if (requestCode == REQ_LOAD && resultCode == Activity.RESULT_OK) {
            try{
                if(data == null || data.data == null ) throw IllegalArgumentException("FILE_LOAD_ERR")
                val ips = contentResolver.openInputStream(data.data)
                ips?:throw IllegalArgumentException("FILE_LOAD_ERR")
                val loadString = ips.readBytes().toString( Charsets.UTF_8)
                ips.close()

                LaqSettingManager().setLaqSetting(loadString,Repo)

                SC.needResetTwoStepSetting = true
                SC.needResetBgSetting = true
                SC.needResetUxSetting = true

            } catch (e:Exception) {
                e.printStackTrace()
                "파일을 불러올 수 없습니다".toast()
            }

            "설정 불러오기 완료".toast()
        }
    }


}
