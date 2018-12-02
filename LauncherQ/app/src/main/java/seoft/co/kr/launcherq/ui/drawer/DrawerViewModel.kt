package seoft.co.kr.launcherq.ui.drawer

import android.databinding.ObservableField
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.data.model.CommonApp
import seoft.co.kr.launcherq.ui.MsgType
import seoft.co.kr.launcherq.ui.ViewModelHelper
import seoft.co.kr.launcherq.utill.InstalledAppUtil
import seoft.co.kr.launcherq.utill.SC
import seoft.co.kr.launcherq.utill.i
import seoft.co.kr.launcherq.utill.value

class DrawerViewModel(val repo: Repo): ViewModelHelper() {

    val TAG = "DrawerViewModel#$#"

    lateinit var drawerApps : MutableList<CommonApp>
    var drawerMode = DrawerMode.LAUNCH_MODE

    override fun start() {
        isChangedAppsWithRefresh()
        loadDrawerList()
    }

    fun onRestartInVM(){
        if(isChangedAppsWithRefresh()){
            loadDrawerList()
        }
    }

    fun loadDrawerList(isHideList:Boolean = false){

        toActMsg(MsgType.UPDATE_APPS,
            DrawerLoadInfo(
                drawerApps
                    .filter { it.isHide == isHideList  }
                    .toMutableList(),
                repo.preference.getDrawerItemNum(),
                repo.preference.getDrawerColumnNum()
            )
        )
    }

    fun isChangedAppsWithRefresh() : Boolean {

        val instApps = InstalledAppUtil().getInstalledApps()
        drawerApps = repo.preference.getDrawerApps()
        var isChanged = false
        var tmpRst : Boolean

        // remove deleted app in drawerApps & add new installed app to drawerApps
        drawerApps = drawerApps
            .filter { cApp ->
                tmpRst = instApps.any { iApp ->
                    iApp.pkgName == cApp.pkgName
                }
                if(!tmpRst) isChanged = true
                tmpRst
            }
            .toMutableList()
            .apply {
                this.addAll(
                    instApps
                        .filter { cApp ->
                            tmpRst = !drawerApps
                                .any{ iApp ->
                                    iApp.pkgName == cApp.pkgName
                                }
                            if(tmpRst) isChanged = true
                            tmpRst

                        }
                        .toList()
                )
            }

        save()

        return isChanged
    }

    fun setHide(pkgName: String) {
        drawerApps
            .forEach {
                if(it.pkgName == pkgName) it.isHide = true
            }
        save()
        loadDrawerList()

    }

    fun setUnhide(pkgName: String) {
        drawerApps
            .forEach {
                if(it.pkgName == pkgName) it.isHide = false
            }
        save()
        loadDrawerList(true)
    }

    fun moveApp(fromPkgName : String, toPkgName: String) {
        val fromIdx = getIndexFromPkgName(fromPkgName)
        val toIdx = getIndexFromPkgName(toPkgName)

        if(fromIdx < toIdx)
            drawerApps.add(toIdx + 1, drawerApps.get(fromIdx))
        else
            drawerApps.add(toIdx, drawerApps.get(fromIdx))

        drawerApps.removeAt(
            if(fromIdx < toIdx) fromIdx else fromIdx + 1
        )
        save()
        loadDrawerList()

    }

    fun getIndexFromPkgName(pkgName_:String) = drawerApps.indexOfFirst{ it.pkgName == pkgName_ }

    fun save(){ repo.preference.setDrawerApps(drawerApps) }

    fun clickOptionBt(){
        toActMsg(MsgType.SHOW_SETTING_DAILOG)
    }

    fun onTextChanged(searchText:CharSequence) {

        drawerMode.i(TAG)

        if(drawerMode == DrawerMode.HIDE_MODE) return

        toActMsg(MsgType.UPDATE_APPS,
            DrawerLoadInfo(
                drawerApps
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