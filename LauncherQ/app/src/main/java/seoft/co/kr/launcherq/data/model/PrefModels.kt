package seoft.co.kr.launcherq.data.model

import android.content.Intent.*

data class QuickApp(
    var commonApp: CommonApp,
    var type: QuickAppType,
    var cmds: MutableList<String> = mutableListOf(),
    var expert: Expert? = null,
    var isPicked: Boolean = false,
    var hasImg: Boolean = false
)

data class Expert(
    /**
     * not null -> use this
     * null check commonApp was empty ->
     *                                  not empty -> use that
     *                                  empty -> not use
     *
     * each property create from ExpertSettingActivity ( if use that )
     *
     */
    var useOne:CustomIntent?,
    var useTwo:MutableList<CustomIntent?>? = null
)

data class CustomIntent(

    /**
     * empty or exist checking property
     * "" > empty
     * else > exist
     */
    var name:String,

    var action:String = ACTION_DEFAULT,
    var uriData:String? = null,
    var type:String? = null,
    var categorys: MutableList<String> = arrayListOf(CATEGORY_DEFAULT),
    var flag : Int = FLAG_GRANT_READ_URI_PERMISSION,
    var addFlag : MutableList<Int>? = null,
    var pkgName :String ? = null,
    var customComponentName:CustomComponentName? = null
) {
    fun checkLaunchDefaultMethod()
        = ( uriData == null &&
            type == null &&
            addFlag == null &&
            customComponentName == null &&
            categorys.size == 1 &&
            categorys.get(0) == CATEGORY_DEFAULT &&
            pkgName != null )


}

data class CustomComponentName(
    var compName : String,
    var compCls : String
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

