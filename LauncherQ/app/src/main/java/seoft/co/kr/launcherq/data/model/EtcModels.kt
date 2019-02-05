package seoft.co.kr.launcherq.data.model

import android.content.pm.ShortcutInfo

data class ExpertOption(
    var name:String,
    var result:String
)

data class Shortcut(val id: String, val packageName: String, val label: String, val shortcutInfo: ShortcutInfo)
