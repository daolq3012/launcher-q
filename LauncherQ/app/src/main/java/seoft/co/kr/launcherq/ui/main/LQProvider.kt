package seoft.co.kr.launcherq.ui.main

import android.content.*
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.text.TextUtils
import seoft.co.kr.launcherq.utill.i

class LQProvider :ContentProvider(){

    val TAG = "LQProvider#$#"

    val PKG_NAME = "pkgName"
    val INFO = "info"
    val _ID = "_id"
    val PROVIDER_NAME = "seoft.co.kr.launcherq.LQProvider"
    val URL = "content://$PROVIDER_NAME/apps"
    val CONTENT_URI = Uri.parse(URL)

    private val APP_PROJECTION_MAP: HashMap<String, String>? = null

    val APPS = 1
    val APP_ID = 2

    val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        .apply {
            addURI(PROVIDER_NAME, "apps", APPS)
            addURI(PROVIDER_NAME, "apps/#", APP_ID)
        }

    var db: SQLiteDatabase? = null
    val DATABASE_NAME = "LQ"
    val APPS_TABLE_NAME = "apps"
    val DATABASE_VERSION = 1
    val CREATE_DB_TABLE = " CREATE TABLE $APPS_TABLE_NAME ($_ID INTEGER PRIMARY KEY AUTOINCREMENT, $PKG_NAME TEXT NOT NULL, $INFO TEXT NOT NULL);"


    inner class DatabaseHelper(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {

        constructor(context_: Context?) :this(context_, DATABASE_NAME,null, DATABASE_VERSION)

        override fun onCreate(db: SQLiteDatabase?) {
            db?.execSQL(CREATE_DB_TABLE)

            "DatabaseHelper onCreate".i(TAG)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db?.execSQL("DROP TABLE IF EXISTS $APPS_TABLE_NAME")
            onCreate(db)
        }

    }

    override fun onCreate(): Boolean {
        val dbHelper = DatabaseHelper(context)
        db = dbHelper.writableDatabase

        "onCreate".i(TAG)

        return db != null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {

        val rowID = db?.insert(APPS_TABLE_NAME, "", values)

        /**
         * If record is added successfully
         */

        rowID?.let {
            if (rowID > 0) {
                val _uri = ContentUris.withAppendedId(CONTENT_URI, rowID)
                context!!.contentResolver.notifyChange(_uri, null)
                return _uri
            }
        }

        throw SQLException("Failed to add a record into $uri")
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder_: String? ): Cursor? {
        val qb = SQLiteQueryBuilder()
        qb.tables = APPS_TABLE_NAME

        when (uriMatcher.match(uri)) {
            APPS -> qb.setProjectionMap(APP_PROJECTION_MAP)
            APP_ID -> qb.appendWhere("$_ID=${uri.pathSegments.get(1)}")
        }

        var sortOrder = ""


        if(sortOrder_.isNullOrBlank()) {
            sortOrder = PKG_NAME
        }

        val c = qb.query(db,projection,selection,selectionArgs,null,null, sortOrder)
            .apply { setNotificationUri(context.contentResolver,uri) }
        return c
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        var cnt = 0

        when(uriMatcher.match(uri)) {
            APPS -> {
                db?.let {
                    cnt = it.update(APPS_TABLE_NAME,values,selection,selectionArgs)
                }
            }
            APP_ID -> {
                db?.let {
                    val q = "$_ID = ${uri.pathSegments.get(1)}${if(!TextUtils.isEmpty(selection)) " AND ($selection)" else ""}"
                    cnt = it.update(APPS_TABLE_NAME,values,q ,selectionArgs)
                }
            }
        }

        context.contentResolver.notifyChange(uri,null)
        return cnt
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        var cnt = 0

        when(uriMatcher.match(uri)) {
            APPS -> {
                db?.let {
                    cnt = it.delete(APPS_TABLE_NAME, selection, selectionArgs)
                }
            }
            APP_ID -> {
                db?.let {
                    val q = "$_ID = ${uri.pathSegments.get(1)}${if(!TextUtils.isEmpty(selection)) " AND ($selection)" else ""}"
                    cnt = it.delete(APPS_TABLE_NAME, q, selectionArgs)
                }
            }
        }

        context.contentResolver.notifyChange(uri,null)
        return cnt
    }

    override fun getType(uri: Uri): String? {
        return when(uriMatcher.match(uri)) {
            APPS -> "vnd.android.cursor.dir/vnd.launcherq.apps"
            APP_ID -> "vnd.android.cursor.item/vnd.launcherq.apps"
            else -> throw IllegalArgumentException("Unsupported URI: $uri");
        }
    }

}