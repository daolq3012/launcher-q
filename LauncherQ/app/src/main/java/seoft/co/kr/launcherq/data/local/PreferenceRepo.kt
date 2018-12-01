package seoft.co.kr.launcherq.data.local

import android.content.Context
import android.preference.PreferenceManager
import com.google.gson.reflect.TypeToken
import seoft.co.kr.launcherq.data.model.BackgroundWidgetInfos
import seoft.co.kr.launcherq.data.model.CommonApp
import seoft.co.kr.launcherq.data.model.Info
import seoft.co.kr.launcherq.utill.App
import seoft.co.kr.launcherq.utill.SC
import seoft.co.kr.launcherq.utill.i

class PreferenceRepo {

    private val DEFAULT_DEVICE_X = 1080
    private val DEFAULT_DEVICE_Y = 1920

    private val SP_IS_FIRST_LAUNCH = "PFRK_IS_FIRST_LAUNCH"
    private val mPrefs = PreferenceManager.getDefaultSharedPreferences(App.get)
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
    fun getDrawerItemNum() = mPrefs.getInt(SP_DRAWER_ITEM_NUM,25) // default is 5

    fun setBgWidgetInfos(bgwi: BackgroundWidgetInfos) {
        mPrefs.edit().putString(SP_BG_WIDGET_INFOS,SC.gson.toJson(bgwi)).apply()
    }
    fun getBgWidgetInfos() :BackgroundWidgetInfos {
        var jsonStr = mPrefs.getString(SP_BG_WIDGET_INFOS,"")
        if (jsonStr.isNullOrEmpty()) {
            val backgroundInfos = BackgroundWidgetInfos( arrayOf(
                Info("HH:mm",20,"#00574B",50,100,""),
                Info("AM%%PM",20,"#00574B",250,200,""),
                Info("yyyy. MM. dd.",20,"#00574B",50,300,""),
                Info("Sun%%Mon%%Tue%%Wed%%Thu%%Fri%%Sat",20,"#00574B",250,400,""),
                Info("WELCOME%%TO%%LAUNCHER Q",20,"#00574B",50,500,"")
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





}


