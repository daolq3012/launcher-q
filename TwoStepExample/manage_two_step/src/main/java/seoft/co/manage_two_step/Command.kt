package seoft.co.manage_two_step

import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns


data class Command(
    var id : Long? = null,
    var title:String,
    var pkgName:String,
    var cls:String,
    var normalMessage:String="",
    var useEdit:Boolean=false
) {
    companion object {

        /////////////////////////////////////////////////
        // in CommandContentProvider's companion objects in LauncherQ
        val AUTHORITY = "seoft.co.kr.launcherq.utill.CommandContentProvider"
        val URI_COMMAND = Uri.parse("content://$AUTHORITY/${Command.TABLE_NAME}")
        fun getUriFromPkgName(pkgName:String): Uri {
            return Uri.parse("content://$AUTHORITY/${Command.TABLE_NAME}/-1?${Command.COLUMN_PKG_NAME}=$pkgName")
        }
        //
        /////////////////////////////////////////////////


        const val NORMAL_MESSAGE = "NORMAL_MESSAGE"
        const val EDIT_MESSAGE = "EDIT_MESSAGE"

        const val TABLE_NAME = "COMMAND"
        const val COLUMN_ID = BaseColumns._ID
        const val COLUMN_TITLE = "title"
        const val COLUMN_PKG_NAME = "pkg_name"
        const val COLUMN_CLASS = "cls"
        const val COLUMN_NORMAL_MSG = "normal_message"
        const val COLUMN_USE_EDIT= "use_text"

        fun fromContentValues(values: ContentValues) : Command {
            with(values) {
                return@fromContentValues Command(
                    if (containsKey(COLUMN_ID)) getAsLong(COLUMN_ID) else null,
                    if (containsKey(COLUMN_TITLE)) getAsString(COLUMN_TITLE) else "",
                    if (containsKey(COLUMN_PKG_NAME)) getAsString(COLUMN_PKG_NAME) else "",
                    if (containsKey(COLUMN_CLASS)) getAsString(COLUMN_CLASS) else "",
                    if (containsKey(COLUMN_NORMAL_MSG)) getAsString(COLUMN_NORMAL_MSG) else "",
                    if (containsKey(COLUMN_USE_EDIT)) getAsBoolean(COLUMN_USE_EDIT) else false
                )
            }
        }

        fun toContentValues(cmd:Command) : ContentValues {
            return ContentValues()
                .apply {
                    put(COLUMN_TITLE,cmd.title)
                    put(COLUMN_PKG_NAME,cmd.pkgName)
                    put(COLUMN_CLASS,cmd.cls)
                    put(COLUMN_NORMAL_MSG,cmd.normalMessage)
                    put(COLUMN_USE_EDIT,cmd.useEdit)
                }
        }


        fun cursorToCommands(cursor: Cursor) : List<Command> {
            val cmds = mutableListOf<Command>()
            with(cursor){
                while(moveToNext()){
                    cmds.add(Command(
                        getLong(getColumnIndex(Command.COLUMN_ID)),
                        getString(getColumnIndex(Command.COLUMN_TITLE)),
                        getString(getColumnIndex(Command.COLUMN_PKG_NAME)),
                        getString(getColumnIndex(Command.COLUMN_CLASS)),
                        getString(getColumnIndex(Command.COLUMN_NORMAL_MSG)),
                        ( getInt(getColumnIndex(Command.COLUMN_USE_EDIT)) == 0 )
                    ))
                }
                return cmds
            }
        }

    }
}