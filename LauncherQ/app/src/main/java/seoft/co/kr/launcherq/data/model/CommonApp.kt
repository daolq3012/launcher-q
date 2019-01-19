package seoft.co.kr.launcherq.data.model

data class CommonApp(
    val pkgName:String,
    val label:String = "",
    val detailName :String = "",
    var isHide:Boolean = false,
    var isExcept:Boolean = false
)

/**
 * Have exception :
 * drawer -> pkgName : DRAWER
 */
enum class CAppException(val get:String){
    DRAWER("DRAWER"),
    TEST("TEST"),
}

val SPLITTER = "#$#"
fun CommonApp.toSaveString() = "${pkgName}${SPLITTER}${label}${SPLITTER}${detailName}"
fun String.toCommonApp() = CommonApp(this.split(SPLITTER)[0],this.split(SPLITTER)[1],this.split(SPLITTER)[2],false,false)
