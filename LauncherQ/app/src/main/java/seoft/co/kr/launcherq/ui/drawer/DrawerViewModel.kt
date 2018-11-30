package seoft.co.kr.launcherq.ui.drawer

import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.data.model.CommonApp
import seoft.co.kr.launcherq.ui.MsgType
import seoft.co.kr.launcherq.ui.ViewModelHelper
import seoft.co.kr.launcherq.utill.App
import seoft.co.kr.launcherq.utill.InstalledAppUtil
import seoft.co.kr.launcherq.utill.i

class DrawerViewModel(val repo: Repo): ViewModelHelper() {

    val TAG = "DrawerViewModel#$#"

    val dam by lazy { DrawerAppManager(repo) }

    override fun start() {

        if(dam.isChangedAppsWithRefresh() ){

        }

        toActMsg(MsgType.UPDATE_APPS,dam.drawerApps)






    }


}