package seoft.co.kr.launcherq.data.local

import android.content.Context
import android.preference.PreferenceManager
import seoft.co.kr.launcherq.utill.App

class PreferenceRepo {

    private val DEFAULT_DEVICE_X = 1080
    private val DEFAULT_DEVICE_Y = 1920

    private val SP_IS_FIRST_LAUNCH = "PFRK_IS_FIRST_LAUNCH"
    private val mPrefs = PreferenceManager.getDefaultSharedPreferences(App.get)
    private val SP_BG_IMAGE_BITMAP_PATH = "SP_BG_IMAGE_BITMAP_PATH"
    private val SP_BG_IMAGE_COLOR = "SP_BG_IMAGE_COLOR"
    private val SP_DEVICE_X = "SP_DEVICE_X"
    private val SP_DEVICE_Y = "SP_DEVICE_Y"



    fun isFirstLaunch()
            = mPrefs.getBoolean(SP_IS_FIRST_LAUNCH,true)
    fun setIsFirst(isFirst: Boolean) {
        mPrefs.edit().putBoolean(SP_IS_FIRST_LAUNCH,isFirst).apply()
    }

    fun setBgImageBitmapPath(str:String) {
        mPrefs.edit().putString(SP_BG_IMAGE_BITMAP_PATH,str).apply()
    }
    fun getBgImageBitmapPath()
        = mPrefs.getString(SP_BG_IMAGE_BITMAP_PATH,"")

    fun setBgImageColor(str:String) {
        mPrefs.edit().putString(SP_BG_IMAGE_COLOR,str).apply()
    }
    fun getBgImageColor()
            = mPrefs.getString(SP_BG_IMAGE_COLOR,"#FFFFFF")


    fun setDeviceX(i:Int) {
        mPrefs.edit().putInt(SP_DEVICE_X,i).apply()
    }

    fun setDeviceY(i:Int) {
        mPrefs.edit().putInt(SP_DEVICE_Y,i).apply()
    }

    fun getDeviceX() = mPrefs.getInt(SP_DEVICE_X,DEFAULT_DEVICE_X)
    fun getDeviceY() = mPrefs.getInt(SP_DEVICE_Y,DEFAULT_DEVICE_Y)




}