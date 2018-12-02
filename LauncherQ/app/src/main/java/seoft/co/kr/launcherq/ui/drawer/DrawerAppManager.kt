package seoft.co.kr.launcherq.ui.drawer

import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.data.model.CommonApp
import seoft.co.kr.launcherq.utill.App
import seoft.co.kr.launcherq.utill.InstalledAppUtil
import seoft.co.kr.launcherq.utill.i

class DrawerAppManager(val repo:Repo){

    val TAG = "DrawerAppManager#$#"
    lateinit var drawerApps : MutableList<CommonApp>

    /**
     * @description
     * 1. check add or remove app
     * 2. if exist
     * 3. refresh drawer apps ( remove or add app )
     * @return find add or deleted app
     */
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
    }

    fun setUnhide(pkgName: String) {
        drawerApps
            .forEach {
                if(it.pkgName == pkgName) it.isHide = false
            }
        save()
    }

    /**
     * @descript : need to get idx from pkgName searching in list
     */
    fun moveApp(fromIdx : Int, toIdx: Int) {
        drawerApps.add(toIdx, drawerApps.get(fromIdx))
        drawerApps.removeAt(
            if(fromIdx < toIdx) fromIdx else fromIdx + 1
        )
        save()
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
    }

    fun getIndexFromPkgName(pkgName_:String) = drawerApps.indexOfFirst{ it.pkgName == pkgName_ }

    fun save(){ repo.preference.setDrawerApps(drawerApps) }


}