package seoft.co.kr.twostepexample

import android.content.Context

object CommandRepo{

    /**
     * return value is add item count
     */
    fun insertCommands(context: Context, cmds : List<Command>): Int {

        return context.contentResolver.bulkInsert(
            Command.URI_COMMAND,
            cmds.map { Command.toContentValues(it) }
                .toTypedArray() )
    }

    fun selectFromPkgName(context: Context, pkgName:String): List<Command> {
        val c = context.contentResolver.query(Command.getUriFromPkgName(pkgName),null,null,null,null)
        return Command.cursorToCommands(c)
    }

    fun deleteFromPkgName(context: Context, pkgName:String) : Int {
        return context.contentResolver.delete(Command.getUriFromPkgName(pkgName),null,null)
    }


}