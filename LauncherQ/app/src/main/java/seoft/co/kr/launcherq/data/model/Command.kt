package seoft.co.kr.launcherq.data.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns

@Entity(tableName = Command.TABLE_NAME)
data class Command(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    var id : Long? = null,

    @ColumnInfo(name = COLUMN_TITLE)
    var title:String,

    @ColumnInfo(name = COLUMN_PKG_NAME)
    var pkgName:String,

    @ColumnInfo(name = COLUMN_CLASS)
    var cls:String,

    @ColumnInfo(name = COLUMN_NORMAL_MSG)
    var normalMessage:String="",

    @ColumnInfo(name = COLUMN_USE_EDIT)
    var useEdit:Boolean=false

    ) {
    companion object {
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