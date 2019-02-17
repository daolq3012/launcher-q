package seoft.co.kr.twostepexample

import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns

data class Command(
    var id : Long? = null,
    var pkgName:String,
    var cls:String,
    var normalMessage:String="",
    var useEdit:Boolean=false,
    var editMessage:String=""
    ) {
    companion object {

        // in CommandContentProvider's companion objects in LauncherQ
        val AUTHORITY = "seoft.co.kr.launcherq.utill.CommandContentProvider"
        val URI_COMMAND = Uri.parse("content://$AUTHORITY/${Command.TABLE_NAME}")
        fun getUriFromId(id:Long): Uri {
            return Uri.parse("content://$AUTHORITY/${Command.TABLE_NAME}/$id")
        }

        const val TABLE_NAME = "COMMAND"
        const val COLUMN_ID = BaseColumns._ID
        const val COLUMN_PKG_NAME = "pkg_name"
        const val COLUMN_CLASS = "cls"
        const val COLUMN_NORMAL_MSG = "normalMessage"
        const val COLUMN_EDIT_MSG = "editMessage"
        const val COLUMN_USE_EDIT= "use_text"

        fun fromContentValues(values: ContentValues) : Command {
            with(values) {
                return@fromContentValues Command(
                    if (containsKey(COLUMN_ID)) getAsLong(COLUMN_ID) else null,
                    if (containsKey(COLUMN_PKG_NAME)) getAsString(COLUMN_PKG_NAME) else "",
                    if (containsKey(COLUMN_CLASS)) getAsString(COLUMN_CLASS) else "",
                    if (containsKey(COLUMN_NORMAL_MSG)) getAsString(COLUMN_NORMAL_MSG) else "",
                    if (containsKey(COLUMN_USE_EDIT)) getAsBoolean(COLUMN_USE_EDIT) else false,
                    if (containsKey(COLUMN_EDIT_MSG)) getAsString(COLUMN_EDIT_MSG) else ""
                )
            }
        }

        fun toContentValues(cmd:Command) : ContentValues {
            return ContentValues()
                .apply {
                    put(COLUMN_PKG_NAME,cmd.pkgName)
                    put(COLUMN_CLASS,cmd.cls)
                    put(COLUMN_NORMAL_MSG,cmd.normalMessage)
                    put(COLUMN_USE_EDIT,cmd.useEdit)
                    put(COLUMN_EDIT_MSG,cmd.editMessage)
                }
        }

        fun cursorToCommands(cursor: Cursor) : List<Command> {
            val cmds = mutableListOf<Command>()
            with(cursor){
                while(moveToNext()){
                    cmds.add(Command(
                        getLong(getColumnIndex(Command.COLUMN_ID)),
                        getString(getColumnIndex(Command.COLUMN_PKG_NAME)),
                        getString(getColumnIndex(Command.COLUMN_CLASS)),
                        getString(getColumnIndex(Command.COLUMN_NORMAL_MSG)),
                        ( getInt(getColumnIndex(Command.COLUMN_USE_EDIT)) == 0 ),
                        getString(getColumnIndex(Command.COLUMN_EDIT_MSG))
                    ))
                }
                return cmds
            }
        }

    }
}