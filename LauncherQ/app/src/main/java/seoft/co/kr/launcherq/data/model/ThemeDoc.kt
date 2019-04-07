package seoft.co.kr.launcherq.data.model

import java.sql.Timestamp

data class ThemeDoc(
    val id:Int,
    val writerId:String,
    val title:String,
    val content:String = "",
    var tag:String = "",
    var updateTime: Timestamp,
    val price:Int=0,
    val likeCnt : Int=0,
    val commentCnt : Int=0,
    var themeInfos: Array<ThemeInfo>? = null
)
/*
{
    // server is here
    var themeInfos: Array<ThemeInfo>? = null
}
*/

data class ThemeInfo(
    val id:Int,
    val type:Int,
    val etc:String,
    val size:Int,
    val posX:Int,
    val posY:Int,
    val color:String,
    val font:String
)
