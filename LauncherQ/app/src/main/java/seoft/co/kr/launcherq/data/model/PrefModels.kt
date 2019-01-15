package seoft.co.kr.launcherq.data.model

data class QuickApp(
    var commonApp: CommonApp,
    var type: QuickAppType,
    var cmds: MutableList<String> = mutableListOf(),
    var expert: Expert? = null,
    var isPicked: Boolean = false,
    var hasImg: Boolean = false


)

data class Expert(
    var useOne:CustomIntent?,
    var useTwo:MutableList<CustomIntent>
)

data class CustomIntent(
    var name:String,
    var action:String? = null,
    var uriData:String? = null,
    var type:String? = null,
    var categorys: MutableList<String> = mutableListOf(),
    var flag : Int = -1,
    var addFlag : MutableList<Int> = mutableListOf(),
    var pkgName :String ? = null,
    var customComponentName:CustomComponentName? = null,
    var className : ClassName? = null
)

data class CustomComponentName(
    var compName : String? = null,
    var compCls : String? = null
)

data class ClassName(
    var pkgName : String? = null,
    var clsName : String? = null
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

