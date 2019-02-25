package seoft.co.kr.launcherq.ui.drawer

import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.ui.MsgType
import seoft.co.kr.launcherq.ui.ViewModelHelper
import seoft.co.kr.launcherq.utill.SC

class DrawerViewModel(val repo: Repo): ViewModelHelper() {

    val TAG = "DrawerViewModel#$#"

    var drawerMode = DrawerMode.LAUNCH_MODE

    override fun start() {
        loadDrawerList()
    }

    fun loadDrawerList(isHideList:Boolean = false){
        toActMsg(MsgType.UPDATE_APPS,
            DrawerLoadInfo(
                SC.drawerApps
                    .filter { it.isHide == isHideList  }
                    .toMutableList(),
                repo.preference.getDrawerItemNum(),
                repo.preference.getDrawerColumnNum()
            )
        )
    }

    fun setHide(pkgName: String) {
        SC.drawerApps
            .forEach {
                if(it.pkgName == pkgName) it.isHide = true
            }
        save()
        loadDrawerList()

    }

    fun setUnhide(pkgName: String) {
        SC.drawerApps
            .forEach {
                if(it.pkgName == pkgName) it.isHide = false
            }
        save()
        loadDrawerList(true)
    }

    fun moveApp(fromPkgName : String, toPkgName: String) {
        val fromIdx = getIndexFromPkgName(fromPkgName)
        val toIdx = getIndexFromPkgName(toPkgName)

        if(fromIdx < toIdx) SC.drawerApps.add(toIdx + 1, SC.drawerApps.get(fromIdx))
        else SC.drawerApps.add(toIdx, SC.drawerApps.get(fromIdx))

        SC.drawerApps.removeAt( if(fromIdx < toIdx) fromIdx else fromIdx + 1 )
        save()
        loadDrawerList()

    }

    fun getIndexFromPkgName(pkgName_:String) = SC.drawerApps.indexOfFirst{ it.pkgName == pkgName_ }

    fun save(){ repo.preference.setDrawerApps(SC.drawerApps) }

    fun clickOptionBt(){
        toActMsg(MsgType.SHOW_SETTING_DAILOG)
    }

    fun onTextChanged(searchText:CharSequence) {

        if(drawerMode == DrawerMode.HIDE_MODE) return

        toActMsg(MsgType.UPDATE_APPS,
            DrawerLoadInfo(
                SC.drawerApps
                    .filter { it.label.toLowerCase().contains(searchText.toString())  }
                    .filter { !it.isHide   }
                    .toMutableList(),
                repo.preference.getDrawerItemNum(),
                repo.preference.getDrawerColumnNum()
            )
        )
    }

    fun clickClearSearchText() {
        if(drawerMode == DrawerMode.HIDE_MODE) return

        onTextChanged("")
        toActMsg(MsgType.CLEAR_SEARCH_EDIT_TEXT)
    }

    enum class DrawerMode{
        LAUNCH_MODE,
        ADD_MODE,
        MOVE_MODE,
        HIDE_MODE
    }

}