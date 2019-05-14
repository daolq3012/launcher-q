package seoft.co.kr.launcherq.utill

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.data.model.CommonApp

class PackageReceiver: BroadcastReceiver(){

    val TAG = "PackageReceiver#$#"

    override fun onReceive(context: Context, intent: Intent) {

        val packageName = intent.data.schemeSpecificPart
        val action = intent.action

        if (action == Intent.ACTION_PACKAGE_ADDED) {
            "ACTION_PACKAGE_ADDED : $packageName".i(TAG)

            val packageManager = App.get.packageManager

            val isRunApp = InstantUtil().getInstalledApps()
                .asSequence()
                .map { it.pkgName }
                .contains(packageName)

            if(!isRunApp) return

            SC.drawerApps = Repo.preference.getDrawerApps()

            val isInDrawer = SC.drawerApps
                .asSequence()
                .map { it.pkgName }
                .contains ( packageName )

            if(isInDrawer) return

            val appInfo = packageManager.getApplicationInfo(packageName,0)
            val label = packageManager.getApplicationLabel(appInfo).toString()
            SC.drawerApps
                .forEach {
                    Repo.imageCacheRepo.saveCache(it.pkgName,App.get.packageManager.getApplicationIcon(it.pkgName))
                }
            SC.drawerApps.add(CommonApp(packageName, label ))
            Repo.preference.setDrawerApps(SC.drawerApps)

        } else if (action == Intent.ACTION_PACKAGE_REPLACED) {
            "ACTION_PACKAGE_REPLACED : $packageName".i(TAG)
            // ??
        } else if (action == Intent.ACTION_PACKAGE_FULLY_REMOVED) {
            "ACTION_PACKAGE_FULLY_REMOVED : $packageName".i(TAG)
            SC.drawerApps = Repo.preference.getDrawerApps()
                .asSequence()
                .filterNot { it.pkgName == packageName }
                .toMutableList()
            Repo.preference.setDrawerApps(SC.drawerApps)

            for(i in 0 until 4) {
                val tmpQApps = Repo.preference.getQuickApps(i)
                for(j in 0 until 16) if (tmpQApps[j].commonApp.pkgName == packageName) tmpQApps[j] = SC.getQuickAppFactory()
                Repo.preference.setQuickApps(tmpQApps,i)
            }

        }

    }




}