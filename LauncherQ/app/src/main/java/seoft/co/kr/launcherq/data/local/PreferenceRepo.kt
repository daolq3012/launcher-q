package seoft.co.kr.launcherq.data.local

import android.content.Context
import android.preference.PreferenceManager
import seoft.co.kr.launcherq.utill.App

class PreferenceRepo {

    private val PFRK_IS_FIRST_LAUNCH = "PFRK_IS_FIRST_LAUNCH"
    private val mPrefs = PreferenceManager.getDefaultSharedPreferences(App.get)

    fun isFirstLaunch()
            = mPrefs.getBoolean(PFRK_IS_FIRST_LAUNCH,true)

    fun setIsFirst(isFirst: Boolean) {
        mPrefs.edit().putBoolean(PFRK_IS_FIRST_LAUNCH,isFirst).apply()
    }


}