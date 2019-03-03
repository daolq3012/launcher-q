package seoft.co.kr.launcherq.data.model

import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.utill.SC

data class CommonApp(
    val pkgName:String,
    val label:String = "",
//    val detailName :String = "",
    var isHide:Boolean = false,
    var isExcept:Boolean = false
)

/**
 * Have exception :
 * get:String -> pkgName
 */
enum class CAppException(val get:String, val rss:Int, val title:String){
    DRAWER("DRAWER", R.drawable.ic_apps_orange,"서랍"),
    CALL("CALL", R.drawable.ic_call_orange,"전화"),
}

fun CommonApp.toSaveString() = "${pkgName}${SC.SPLITTER}${label}${SC.SPLITTER}"
fun String.toCommonApp() = CommonApp(this.split(SC.SPLITTER)[0],this.split(SC.SPLITTER)[1],false,false)
