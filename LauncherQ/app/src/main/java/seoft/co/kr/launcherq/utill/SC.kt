package seoft.co.kr.launcherq.utill

import android.graphics.Bitmap
import com.google.gson.Gson
import seoft.co.kr.launcherq.data.model.CommonApp
import seoft.co.kr.launcherq.data.model.QuickApp
import seoft.co.kr.launcherq.data.model.QuickAppType

class SC{
    companion object {
        val IMAGE_DIR_NAME = "imageDir"
        val IMAGE_FILE_NAME = "bg.png"

        val ON_WIDGET = "ON"
        val OFF_WIDGET = "OFF"

        var needResetBgSetting = false
        var needResetUxSetting = false
        var needResetTwoStepSetting = false

        val gson = Gson()

//        var test =""
        lateinit var imgDir:String

        var qApp4SetExpert : QuickApp? = null

        var bgBitmap : Bitmap? = null

        lateinit var drawerApps : MutableList<CommonApp>

        val EMPTY_QUICK_APP = QuickApp(CommonApp("","",/*"",*/false), QuickAppType.EMPTY)

    }



}