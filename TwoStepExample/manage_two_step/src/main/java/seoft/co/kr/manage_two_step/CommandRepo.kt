package seoft.co.kr.manage_two_step

import android.content.Context
import java.lang.Exception


object CommandRepo{

    /**
     * return ( value >= 0 )is add item count
     * return value == -1 is fail to insert
     */
    fun insertCommands(context: Context, cmds : List<Command>): Int {
        try {
            return context.contentResolver.bulkInsert(
                Command.URI_COMMAND,
                cmds.map { Command.toContentValues(it) }
                    .toTypedArray() )
        } catch (e: Exception) {
            return -1
        }
    }

    fun selectFromPkgName(context: Context, pkgName:String): List<Command> {
        val c = context.contentResolver.query(Command.getUriFromPkgName(pkgName),null,null,null,null)
        return Command.cursorToCommands(c)
    }

    /**
     * return value == -1 is fail to delete
     */
    fun deleteFromPkgName(context: Context, pkgName:String) : Int {
        try {
            return context.contentResolver.delete(Command.getUriFromPkgName(pkgName),null,null)
        } catch (e: Exception) {
            return -1
        }
    }


}