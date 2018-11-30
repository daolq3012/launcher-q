package seoft.co.kr.launcherq.utill

import android.content.Intent
import seoft.co.kr.launcherq.data.model.CommonApp

class InstalledAppUtil {

    fun getInstalledApps(): MutableList<CommonApp> {

        val packageManager = App.get.packageManager
        val intent = Intent(Intent.ACTION_MAIN, null)
            .apply { addCategory(Intent.CATEGORY_LAUNCHER) }

        return packageManager.queryIntentActivities(intent,0)
            .map {
                it.activityInfo.run {
                    CommonApp(
                        packageName,
                        loadLabel(packageManager).toString(),
                        name,
//                    it.loadIcon(packageManager),
                        false
                    )
                }

            }.toMutableList()
    }

}