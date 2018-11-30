package seoft.co.kr.launcherq.utill

import android.content.Intent
import seoft.co.kr.launcherq.data.model.CommonApp

class InstalledAppUtil {

    fun getInstalledApps(): MutableList<CommonApp> {

        val packageManager = App.get.packageManager
        val intent = Intent(Intent.ACTION_MAIN, null)
            .apply { addCategory(Intent.CATEGORY_LAUNCHER) }

        val packages = packageManager.queryIntentActivities(intent,0)
            .map { it.activityInfo }

        return packages
            .map {
                CommonApp(
                    it.packageName,
                    it.loadLabel(packageManager).toString(),
                    it.name,
                    it.loadIcon(packageManager),
                    false
                )
            }.toMutableList()
    }

}