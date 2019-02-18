package seoft.co.kr.launcherq.data.local

import seoft.co.kr.launcherq.data.model.Command
import seoft.co.kr.launcherq.utill.App
import seoft.co.kr.launcherq.utill.CommandContentProvider

class CommandRepo{


    /**
     * return value is add item count
     */
    fun insertCommands(cmds : List<Command>): Int {

        return App.get.contentResolver.bulkInsert(
            CommandContentProvider.URI_COMMAND,
            cmds.map { Command.toContentValues(it) }
                .toTypedArray() )
    }

    fun selectFromPkgName(pkgName:String): List<Command> {
        val c = App.get.contentResolver.query(CommandContentProvider.getUriFromPkgName(pkgName),null,null,null,null)
        return Command.cursorToCommands(c)
    }

    fun deleteFromPkgName(pkgName:String) : Int {
        return App.get.contentResolver.delete(CommandContentProvider.getUriFromPkgName(pkgName),null,null)
    }



}