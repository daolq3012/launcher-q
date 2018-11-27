package seoft.co.kr.launcherq.data.model

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

enum class WidgetInfoType(var type:Int){
    TIME(0),
    AMPM(1),
    DATE(2),
    DOW(3),
    TEXT(4),

}