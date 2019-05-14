package seoft.co.kr.launcherq.data.local

import android.preference.PreferenceManager
import com.google.gson.reflect.TypeToken
import seoft.co.kr.launcherq.data.model.*
import seoft.co.kr.launcherq.ui.setting.FontActivity
import seoft.co.kr.launcherq.utill.App
import seoft.co.kr.launcherq.utill.SC
import seoft.co.kr.launcherq.utill.toPixel

class PreferenceRepo {

    val TAG = "PreferenceRepo#$#"

    private val DEFAULT_DEVICE_X = 1080
    private val DEFAULT_DEVICE_Y = 1920

    private val mPrefs = PreferenceManager.getDefaultSharedPreferences(App.get)

    private val SP_IS_FIRST_LAUNCH = "PFRK_IS_FIRST_LAUNCH"
    private val SP_BG_IMAGE_BITMAP_PATH = "SP_BG_IMAGE_BITMAP_PATH"
    private val SP_BG_IMAGE_COLOR = "SP_BG_IMAGE_COLOR"
    private val SP_DEVICE_X = "SP_DEVICE_X"
    private val SP_DEVICE_Y = "SP_DEVICE_Y"

    private val SP_BG_WIDGET_INFOS = "SP_BG_WIDGET_INFOS"

    private val SP_BG_DATE_USE = "SP_BG_DATE_USE"
    private val SP_BG_TIME_USE = "SP_BG_TIME_USE"
    private val SP_BG_AMPM_USE = "SP_BG_AMPM_USE"
    private val SP_BG_DOW_USE = "SP_BG_DOW_USE"
    private val SP_BG_TEXT_USE = "SP_BG_TEXT_USE"

    private val SP_DRAWER_APPS = "SP_DRAWER_APPS"
    private val SP_DRAWER_COLUMN_NUM = "SP_DRAWER_COLUMN_NUM"
    private val SP_DRAWER_ITEM_NUM = "SP_DRAWER_ITEM_NUM"

    private val SP_QL_TOP_APPS = "SP_QL_TOP_APPS"
    private val SP_QL_RIGHT_APPS = "SP_QL_RIGHT_APPS"
    private val SP_QL_BOTTOM_APPS = "SP_QL_BOTTOM_APPS"
    private val SP_QL_LEFT_APPS = "SP_QL_LEFT_APPS"

    private val SP_QL_GRID_COUNT = "SP_QL_GRID_COUNT"

    private val SP_QL_GRID_VIEW_SIZE = "SP_QL_GRID_VIEW_SIZE"

    private val SP_QL_DISTANCE= "SP_QL_DISTANCE"

    private val SP_QL_TWO_STEP_OPEN_INTERVAL = "SP_QL_TWO_STEP_OPEN_INTERVAL"

    private val SP_MY_ICON_PIXEL = "SP_MY_ICON_PIXEL"

    private val SP_FLING_TOP_BOUNDARY = "SP_FLING_DOWN_BOUNDARY"
    private val SP_FLING_BOTTOM_BOUNDARY = "SP_FLING_UP_BOUNDARY"


    fun isFirstLaunch() = mPrefs.getBoolean(SP_IS_FIRST_LAUNCH,true)
    fun setIsFirst(isFirst: Boolean) { mPrefs.edit().putBoolean(SP_IS_FIRST_LAUNCH,isFirst).apply() }

    fun setBgImageBitmapPath(str:String) { mPrefs.edit().putString(SP_BG_IMAGE_BITMAP_PATH,str).apply() }
    fun getBgImageBitmapPath() = mPrefs.getString(SP_BG_IMAGE_BITMAP_PATH,"")

    fun setBgImageColor(str:String) { mPrefs.edit().putString(SP_BG_IMAGE_COLOR,str).apply() }
    fun getBgImageColor() = mPrefs.getString(SP_BG_IMAGE_COLOR,"#000000")

    fun setDeviceX(i:Int) { mPrefs.edit().putInt(SP_DEVICE_X,i).apply() }
    fun getDeviceX() = mPrefs.getInt(SP_DEVICE_X,DEFAULT_DEVICE_X)

    fun setDeviceY(i:Int) { mPrefs.edit().putInt(SP_DEVICE_Y,i).apply() }
    fun getDeviceY() = mPrefs.getInt(SP_DEVICE_Y,DEFAULT_DEVICE_Y)

    fun setBgDateUse(b:Boolean) { mPrefs.edit().putBoolean(SP_BG_DATE_USE,b).apply() }
    fun setBgTimeUse(b:Boolean) { mPrefs.edit().putBoolean(SP_BG_TIME_USE,b).apply() }
    fun setBgAmpmUse(b:Boolean) { mPrefs.edit().putBoolean(SP_BG_AMPM_USE,b).apply() }
    fun setBgDowUse(b:Boolean) { mPrefs.edit().putBoolean(SP_BG_DOW_USE,b).apply() }
    fun setBgTextUse(b:Boolean) { mPrefs.edit().putBoolean(SP_BG_TEXT_USE,b).apply() }

    fun getBgDateUse() = mPrefs.getBoolean(SP_BG_DATE_USE,true)
    fun getBgTimeUse() = mPrefs.getBoolean(SP_BG_TIME_USE,true)
    fun getBgAmpmUse() = mPrefs.getBoolean(SP_BG_AMPM_USE,true)
    fun getBgDowUse() = mPrefs.getBoolean(SP_BG_DOW_USE,true)
    fun getBgTextUse() = mPrefs.getBoolean(SP_BG_TEXT_USE,true)

    fun setDrawerColumnNum(num:Int) { mPrefs.edit().putInt(SP_DRAWER_COLUMN_NUM,num).apply() }
    fun getDrawerColumnNum() = mPrefs.getInt(SP_DRAWER_COLUMN_NUM,5) // default is 5

    fun setDrawerItemNum(num:Int) { mPrefs.edit().putInt(SP_DRAWER_ITEM_NUM,num).apply() }
    fun getDrawerItemNum() = mPrefs.getInt(SP_DRAWER_ITEM_NUM,25) // default is 25

    fun setBgWidgetInfos(bgwi: BackgroundWidgetInfos) {
        mPrefs.edit().putString(SP_BG_WIDGET_INFOS,SC.gson.toJson(bgwi)).apply()
    }
    fun getBgWidgetInfos() :BackgroundWidgetInfos {
        var jsonStr = mPrefs.getString(SP_BG_WIDGET_INFOS,"")
        if (jsonStr.isNullOrEmpty()) {

            /**
             * optimization distance & size from that devices
             * 1. Pixel 2 : 1080 * 1920 (420dpi)
             * 2. Pixel 3 XL : 1440 * 1960 (560dpi)
             * 3. Galaxy S10e : 1080 * 2280
             * 4. Galaxy S7 : 1080*1920 AND 1440*2560
             */

//            val aa = 50 / 2
//            val bb = 20 / 2
//            val cc = 200 / 2
//            val dd = 180 / 2
//            val ee = 510 / 2
//            val ff = 330 / 2
//
//            val backgroundInfos = BackgroundWidgetInfos( arrayOf(
//                Info("HH:mm",aa.toPixel(),"#ffffff",(cc*2).toPixel(),cc.toPixel(),FontActivity.DEFAULT_FONT),
//                Info("AM%%PM",aa.toPixel(),"#ffffff",cc.toPixel(),cc.toPixel(),FontActivity.DEFAULT_FONT),
//                Info("yyyy. MM. dd.",bb.toPixel(),"#ffffff",cc.toPixel(),dd.toPixel(),FontActivity.DEFAULT_FONT),
//                Info("Sun%%Mon%%Tue%%Wed%%Thu%%Fri%%Sat",bb.toPixel(),"#ffffff",ee.toPixel(),dd.toPixel(),FontActivity.DEFAULT_FONT),
//                Info("WELCOME TO LAUNCHER Q%%HELLO WORLD",bb.toPixel(),"#f0ff68",cc.toPixel(),ff.toPixel(),FontActivity.DEFAULT_FONT)
//            ))

            val backgroundInfos = BackgroundWidgetInfos( arrayOf(
                Info("HH:mm",17.toPixel(),"#ffffff",154.toPixel(),66.toPixel(),"fonts/roboto_thin.ttf"),
                Info("AM%%PM",17.toPixel(),"#ffffff",66.toPixel(),66.toPixel(),"fonts/roboto_thin.ttf"),
                Info("yyyy. MM. dd.",6.toPixel(),"#ffffff",66.toPixel(),60.toPixel(),"fonts/roboto_light.ttf"),
                Info("Sun%%Mon%%Tue%%Wed%%Thu%%Fri%%Sat",6.toPixel(),"#ffffff",186.toPixel(),60.toPixel(),"fonts/roboto_light.ttf"),
                Info("WELCOME TO LAUNCHER Q%%SET BACKGROUND FROM WIDGET SETTING",6.toPixel(),"#f0ff68",66.toPixel(),210.toPixel(),FontActivity.DEFAULT_FONT)
            ))

            setBgWidgetInfos(backgroundInfos)
            jsonStr = SC.gson.toJson(backgroundInfos)
        }
        return SC.gson.fromJson(jsonStr, BackgroundWidgetInfos::class.java)
    }

    // for drawer


    fun setDrawerApps(apps : MutableList<CommonApp>) {
        mPrefs.edit().putString(SP_DRAWER_APPS, SC.gson.toJson(apps,object : TypeToken<MutableList<CommonApp>>(){}.type)).apply()
    }

    fun getDrawerApps() : MutableList<CommonApp>{
        val jsonStr = mPrefs.getString(SP_DRAWER_APPS,"")
        return SC.gson.fromJson<MutableList<CommonApp>>(jsonStr, object : TypeToken<MutableList<CommonApp>>(){}.type)
    }

    fun setQuickApps(apps : MutableList<QuickApp>,dir:Int) {

        SC.needResetTwoStepSetting = true

        var key = ""
        when(dir) {
            0 -> key = SP_QL_TOP_APPS
            1 -> key = SP_QL_RIGHT_APPS
            2 -> key = SP_QL_BOTTOM_APPS
            3 -> key = SP_QL_LEFT_APPS
        }

        mPrefs.edit().putString(key, SC.gson.toJson(apps,object : TypeToken<MutableList<QuickApp>>(){}.type)).apply()
    }

    fun getQuickApps(dir:Int) : MutableList<QuickApp>{
        var key = ""
        when(dir) {
            0 -> key = SP_QL_TOP_APPS
            1 -> key = SP_QL_RIGHT_APPS
            2 -> key = SP_QL_BOTTOM_APPS
            3 -> key = SP_QL_LEFT_APPS
        }
        var jsonStr = mPrefs.getString(key,"")

        if(jsonStr.isNullOrEmpty()) {
            val tmpApps = mutableListOf<QuickApp>()
            for(i in 0 until 16) tmpApps.add(SC.getQuickAppFactory())

            /**
             * Init default app arrange, if has conatin pkgname app
             * top : dialer, messaging, chrome // if dialer was empty, adjust exception's CALL app
             * right : setting, calendar, youtube
             * left : contact, map, clock
             */

            SC.drawerApps.forEach {
                if(dir==0) {
                    if(it.pkgName.contains("dialer")  ) {
                        tmpApps[0].type = QuickAppType.ONE_APP
                        tmpApps[0].commonApp = it
                    }
                    else if(it.pkgName.contains("messaging")  ) {
                        tmpApps[1].type = QuickAppType.ONE_APP
                        tmpApps[1].commonApp = it
                    }
                    else if(it.pkgName.contains("chrome")) {
                        tmpApps[2].type = QuickAppType.ONE_APP
                        tmpApps[2].commonApp = it
                    }
                } else if (dir==1){
                    if(it.pkgName.contains("setting")) {
                        tmpApps[1].type = QuickAppType.ONE_APP
                        tmpApps[1].commonApp = it
                    } else if(it.pkgName.contains("calendar")) {
                        tmpApps[4].type = QuickAppType.ONE_APP
                        tmpApps[4].commonApp = it
                    } else if(it.pkgName.contains("youtube")) {
                        tmpApps[7].type = QuickAppType.ONE_APP
                        tmpApps[7].commonApp = it
                    }
                } else if (dir==3){
                    if(it.pkgName.contains("contact")) {
                        tmpApps[1].type = QuickAppType.ONE_APP
                        tmpApps[1].commonApp = it
                    } else if(it.pkgName.contains("map")) {
                        tmpApps[4].type = QuickAppType.ONE_APP
                        tmpApps[4].commonApp = it
                    } else if(it.pkgName.contains("clock")) {
                        tmpApps[7].type = QuickAppType.ONE_APP
                        tmpApps[7].commonApp = it
                    }
                }
            }

            if(dir==0 && tmpApps[0].type == QuickAppType.EMPTY) {
                tmpApps[0].type = QuickAppType.ONE_APP
                tmpApps[0].commonApp = CommonApp(CAppException.CALL.get, CAppException.CALL.title, false, true)
            }

            jsonStr = SC.gson.toJson(tmpApps,object : TypeToken<MutableList<QuickApp>>(){}.type)

        }

        return SC.gson.fromJson<MutableList<QuickApp>>(jsonStr, object : TypeToken<MutableList<QuickApp>>(){}.type)
    }

    fun setGridCount(gridCnt : Int) {
        mPrefs.edit().putInt(SP_QL_GRID_COUNT, gridCnt).apply()
    }

    fun getGridCount() : Int{
        return mPrefs.getInt(SP_QL_GRID_COUNT,3)
    }


    fun setGridViewSize(gridViewSize : Int) {
        mPrefs.edit().putInt(SP_QL_GRID_VIEW_SIZE, gridViewSize).apply()
    }
    fun getGridViewSize() : Int{
        return mPrefs.getInt(SP_QL_GRID_VIEW_SIZE,210)
    }

    fun setDistance(distance : Int) {
        mPrefs.edit().putInt(SP_QL_DISTANCE, distance).apply()
    }
    fun getDistance() : Int{
        return mPrefs.getInt(SP_QL_DISTANCE,95)
    }

    fun setTwoStepOpenInterval(interval : Int) {
        mPrefs.edit().putInt(SP_QL_TWO_STEP_OPEN_INTERVAL, interval).apply()
    }
    fun getTwoStepOpenInterval(): Int {
        return mPrefs.getInt(SP_QL_TWO_STEP_OPEN_INTERVAL,10)
    }

    fun setMyIconPixel(size : Int) {
        mPrefs.edit().putInt(SP_MY_ICON_PIXEL, size).apply()
    }
    fun getMyIconPixel(): Int {
        return mPrefs.getInt(SP_MY_ICON_PIXEL,32)
    }

    fun setBottomBoundary(size : Int) {
        mPrefs.edit().putInt(SP_FLING_TOP_BOUNDARY, size).apply()
    }
    fun getBottomBoundary(): Int {
        return mPrefs.getInt(SP_FLING_TOP_BOUNDARY,15)
    }

    fun setTopBoundary(size : Int) {
        mPrefs.edit().putInt(SP_FLING_BOTTOM_BOUNDARY, size).apply()
    }
    fun getTopBoundary(): Int {
        return mPrefs.getInt(SP_FLING_BOTTOM_BOUNDARY,49)
    }

}


