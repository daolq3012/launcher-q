package seoft.co.kr.launcherq.ui.drawer

import seoft.co.kr.launcherq.data.model.CommonApp

data class DrawerLoadInfo(
    val dApps : MutableList<CommonApp>,
    val itemGridNum:Int,
    val columnNum:Int
)
