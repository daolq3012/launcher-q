package seoft.co.kr.launcherq.utill

import android.content.Intent
import android.content.pm.PackageManager
import seoft.co.kr.launcherq.data.model.CommonApp

class InstantUtil {

    fun getInstalledApps(): MutableList<CommonApp> {

        val packageManager = App.get.packageManager
        val intent = Intent(Intent.ACTION_MAIN, null)
            .apply { addCategory(Intent.CATEGORY_LAUNCHER) }

        return packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL)
            .map { it.activityInfo }
            .map { CommonApp( it.packageName, it.loadLabel(packageManager).toString(), false ) }
            .toMutableList()
    }





}