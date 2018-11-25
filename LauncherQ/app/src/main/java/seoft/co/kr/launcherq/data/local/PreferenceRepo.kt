package seoft.co.kr.launcherq.data.local

import android.content.Context
import android.preference.PreferenceManager
import seoft.co.kr.launcherq.utill.App

class PreferenceRepo {

    private val SP_IS_FIRST_LAUNCH = "PFRK_IS_FIRST_LAUNCH"
    private val mPrefs = PreferenceManager.getDefaultSharedPreferences(App.get)
    private val SP_BG_IMAGE_BITMAP_PATH = "SP_BG_IMAGE_BITMAP_PATH"

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

}