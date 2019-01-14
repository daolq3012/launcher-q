package seoft.co.kr.launcherq.data.model

data class CommonApp(

    val pkgName:String,
    val label:String,
    val detailName :String,
    var isHide:Boolean
)

val SPLITTER = "#$#"
fun CommonApp.toSaveString() = "${pkgName}${SPLITTER}${label}${SPLITTER}${detailName}"
fun String.toCommonApp() = CommonApp(this.split(SPLITTER)[0],this.split(SPLITTER)[1],this.split(SPLITTER)[2],false)
