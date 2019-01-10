package seoft.co.kr.launcherq.data.model

data class CommonApp(

    val pkgName:String,
    val label:String,
    val detailName :String,
    var isHide:Boolean // use is pick in arrange image adapter
)
