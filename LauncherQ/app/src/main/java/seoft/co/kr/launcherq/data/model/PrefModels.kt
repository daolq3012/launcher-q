package seoft.co.kr.launcherq.data.model

data class QuickApp(
    var commonApp: CommonApp,
    var type: QuickAppType,
    var cmds: MutableList<String>
)

enum class QuickAppType{
    EMPTY,
    ONE_APP,
    TWO_APP,
    FOLDER,
    EXPERT,
}

data class BackgroundWidgetInfos(
    var Infos : Array<Info>
)

data class Info(
    var etc:String,
    var size:Int,
    var color:String,
    var posX:Int,
    var posY:Int,
    var font:String
)

enum class WidgetInfoType(var getInt:Int,var getStr:String){
    TIME(0,"timeWidget"),
    AMPM(1,"ampmWidget"),
    DATE(2,"dateWidget"),
    DOW(3,"dowWidget"),
    TEXT(4,"textWidget"),
    ERR(5,"err"),
}

