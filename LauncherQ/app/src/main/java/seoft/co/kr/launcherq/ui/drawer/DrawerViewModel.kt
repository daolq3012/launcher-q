package seoft.co.kr.launcherq.ui.drawer

import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.data.model.CommonApp
import seoft.co.kr.launcherq.ui.MsgType
import seoft.co.kr.launcherq.ui.ViewModelHelper
import seoft.co.kr.launcherq.utill.SC
import seoft.co.kr.launcherq.utill.i

class DrawerViewModel(val repo: Repo): ViewModelHelper() {

    val TAG = "DrawerViewModel#$#"

    val dam by lazy { DrawerAppManager(repo) }

    override fun start() {
        dam.isChangedAppsWithRefresh()
        loadDrawerList()
    }

    fun moveApp(fromPkgName : String, toPkgName: String) {
        dam.moveApp(fromPkgName,toPkgName)
        loadDrawerList()
    }
    fun onRestartInVM(){
        if(dam.isChangedAppsWithRefresh()){
            loadDrawerList()
        }
    }

    fun loadDrawerList(isHideList:Boolean = false){

        toActMsg(MsgType.UPDATE_APPS,
            DrawerLoadInfo(
                dam.drawerApps
                    .filter { it.isHide == isHideList  }
                    .toMutableList(),
                repo.preference.getDrawerItemNum(),
                repo.preference.getDrawerColumnNum()
            )
        )
    }

    fun clickOptionBt(){
       toActMsg(MsgType.SHOW_SETTING_DAILOG)
    }

    fun setHideApp(pkgName: String) {
        dam.setHide(pkgName)
        loadDrawerList()
    }

    fun setUnhideApp(pkgName: String) {
        dam.setUnhide(pkgName)
        loadDrawerList(true)
    }


}