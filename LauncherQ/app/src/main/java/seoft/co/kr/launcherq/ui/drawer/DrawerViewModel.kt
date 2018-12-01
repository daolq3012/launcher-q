package seoft.co.kr.launcherq.ui.drawer

import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.ui.MsgType
import seoft.co.kr.launcherq.ui.ViewModelHelper
import seoft.co.kr.launcherq.utill.SC
import seoft.co.kr.launcherq.utill.i

class DrawerViewModel(val repo: Repo): ViewModelHelper() {

    val TAG = "DrawerViewModel#$#"

    val dam by lazy { DrawerAppManager(repo) }

    override fun start() {

        dam.isChangedAppsWithRefresh()

        toActMsg(MsgType.UPDATE_APPS,
            DrawerLoadInfo(
                dam.drawerApps,
                repo.preference.getDrawerItemNum(),
                repo.preference.getDrawerColumnNum()
            )
        )

    }

    fun onResumeInVM(){
        if(dam.isChangedAppsWithRefresh()){
            reloadDrawer()
        }
    }

    fun reloadDrawer(){
        toActMsg(MsgType.UPDATE_APPS,
            DrawerLoadInfo(
                dam.drawerApps,
                repo.preference.getDrawerItemNum(),
                repo.preference.getDrawerColumnNum()
            )
        )
    }

    fun clickOptionBt(){
       toActMsg(MsgType.SHOW_SETTING_DAILOG)
    }



}