package seoft.co.kr.launcherq.data.model

import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.utill.SC
import seoft.co.kr.launcherq.utill.TRANS

data class CommonApp(
    val pkgName:String,
    val label:String = "",
//    val detailName :String = "",
    var isHide:Boolean = false,
    var isExcept:Boolean = false
)

/**
 * Exception app :
 *
 * condition :
 * pkgName is simple string EX) CALL
 * isExcept is true
 * get:String -> pkgName
 */
enum class CAppException(val get:String, val rss:Int, val title:String){
    DRAWER("DRAWER", R.drawable.ic_grid_on, R.string.drawer.TRANS()),
    CALL("CALL", R.drawable.ic_call_im, R.string.call.TRANS()),
}

fun CommonApp.toSaveString() = "${pkgName}${SC.SPLITTER}${label}${SC.SPLITTER}"
fun String.toCommonApp() = CommonApp(this.split(SC.SPLITTER)[0],this.split(SC.SPLITTER)[1],false,false)
