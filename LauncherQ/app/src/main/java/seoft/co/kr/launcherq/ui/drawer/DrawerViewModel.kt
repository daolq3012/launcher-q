package seoft.co.kr.launcherq.ui.drawer

import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.data.model.CommonApp
import seoft.co.kr.launcherq.ui.ViewModelHelper
import seoft.co.kr.launcherq.utill.App
import seoft.co.kr.launcherq.utill.InstalledAppUtil

class DrawerViewModel(val repo: Repo): ViewModelHelper() {

    val TAG = "DrawerViewModel#$#"


    override fun start() {

        ///////////// TODO remove ( in main logic )
        val installedApps = InstalledAppUtil().getInstalledApps()
        repo.preference.setDrawerApps(installedApps)
        /////////////////////////

        val drawerAppManager = DrawerAppManager(repo)








    }


}