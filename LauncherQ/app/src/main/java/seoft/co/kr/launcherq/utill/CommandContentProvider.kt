package seoft.co.kr.launcherq.utill

import android.content.*
import android.database.Cursor
import android.net.Uri
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.data.model.Command
import java.util.*

class CommandContentProvider : ContentProvider(){


    val TAG  = "CommandContentProvider#$#"

    companion object {
        val AUTHORITY = "seoft.co.kr.launcherq.utill.CommandContentProvider"

        val URI_COMMAND = Uri.parse("content://$AUTHORITY/${Command.TABLE_NAME}")

        fun getUriFromPkgName(pkgName:String):Uri{
            return Uri.parse("content://$AUTHORITY/${Command.TABLE_NAME}/-1?${Command.COLUMN_PKG_NAME}=$pkgName")
        }
    }


    /** The match code for some items in the Command table.  */
    private val CODE_COMMAND_DIR = 1

    /** The match code for an item in the Command table.  */
    private val CODE_COMMAND_ITEM = 2

    /** The URI matcher.  */
    private val MATCHER = UriMatcher(UriMatcher.NO_MATCH)
        .apply {
            addURI(AUTHORITY, Command.TABLE_NAME, CODE_COMMAND_DIR)
            addURI(AUTHORITY, Command.TABLE_NAME + "/*", CODE_COMMAND_ITEM)
        }

    override fun onCreate(): Boolean { return true }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        val code = MATCHER.match(uri)

        val commandDao = Repo.localDBRepo.commandDao()
        var cursor:Cursor?

        when(code){
            CODE_COMMAND_DIR -> {
                cursor = commandDao.selectAll()
            }
            CODE_COMMAND_ITEM -> {
                cursor = commandDao.selectByPkgName( uri.getQueryParameter(Command.COLUMN_PKG_NAME))
            }
            else -> cursor = null
        }
        cursor!!.setNotificationUri(context.contentResolver, uri)
        return cursor
    }

    override fun getType(uri: Uri): String? {
        return when(MATCHER.match(uri)){
            CODE_COMMAND_DIR -> "vnd.android.cursor.dir/$AUTHORITY.${Command.TABLE_NAME}"
            CODE_COMMAND_ITEM -> "vnd.android.cursor.item/$AUTHORITY.${Command.TABLE_NAME}"
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues): Uri? {
        when(MATCHER.match(uri)){
            CODE_COMMAND_DIR -> {
                val id = Repo.localDBRepo.commandDao()
                    .insert(Command.fromContentValues(values))
                context.contentResolver.notifyChange(uri,null)
                return ContentUris.withAppendedId(uri,id)
            }
            CODE_COMMAND_ITEM -> throw IllegalArgumentException("Invalid URI, cannot insert with ID: $uri")
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }

    }

    /**
     * not used update
     */
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int
            = throw Exception("not used update method")

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        when(MATCHER.match(uri)){
            CODE_COMMAND_DIR -> throw IllegalArgumentException("Invalid URI, cannot insert with ID: $uri")
            CODE_COMMAND_ITEM -> {
//                val cnt = Repo.localDBRepo.commandDao().deleteById(ContentUris.parseId(uri))
                val cnt = Repo.localDBRepo.commandDao().deleteByPkgName(uri.getQueryParameter(Command.COLUMN_PKG_NAME))
                context.contentResolver.notifyChange(uri,null)
                return cnt
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun applyBatch(operations: ArrayList<ContentProviderOperation>): Array<ContentProviderResult> {

        Repo.localDBRepo.beginTransaction()

        try {
            val results = super.applyBatch(operations)
            Repo.localDBRepo.setTransactionSuccessful()
            return results
        } finally {
            Repo.localDBRepo.endTransaction()
        }
    }

    override fun bulkInsert(uri: Uri, valuesArray: Array<ContentValues>): Int {
        when(MATCHER.match(uri)){
            CODE_COMMAND_DIR -> {

                val commands = arrayListOf<Command>()

                valuesArray.forEach {
                    commands.add(Command.fromContentValues(it))
                }

                return Repo.localDBRepo.commandDao().insertAll(commands.toTypedArray()).size
            }
            CODE_COMMAND_ITEM -> throw IllegalArgumentException("Invalid URI, cannot insert with ID: $uri")
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }


}
