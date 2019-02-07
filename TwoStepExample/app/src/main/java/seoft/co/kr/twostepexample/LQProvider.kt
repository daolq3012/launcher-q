package seoft.co.kr.twostepexample

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.util.Log

class LQProvider(val context:Context){

    val TAG ="LQProvider#$#"

    val PKG_NAME = "pkgName"
    val INFO = "info"
    val _ID = "_id"
    val PROVIDER_NAME = "seoft.co.kr.launcherq.utill.LQProvider"
    val URL = "content://$PROVIDER_NAME/apps"
    val CONTENT_URI = Uri.parse(URL)


    fun select(pkgName:String, info:String="") : List<LQItem> {

        val lqItems = mutableListOf<LQItem>()

        val cursor = if(info.isEmpty()) context.contentResolver.query(CONTENT_URI, arrayOf(_ID,PKG_NAME,INFO),"$PKG_NAME=?",arrayOf(pkgName),null)
            else context.contentResolver.query(CONTENT_URI, arrayOf(_ID,PKG_NAME,INFO),"$PKG_NAME=? AND $INFO=?",arrayOf(pkgName,info),null)
        cursor?.run {
            if(moveToFirst()) {
                while(true){
                    lqItems.add(LQItem(getInt(getColumnIndex(_ID)),getString(getColumnIndex(PKG_NAME)),getString(getColumnIndex(INFO))))
                    if(!moveToNext()) break
                }
            }
        }
        cursor?.close()

        return lqItems
    }

    fun insert(pkgName:String, info:String) :Boolean{
        val insertValue = ContentValues()
            .apply {
                put(PKG_NAME,pkgName)
                put(INFO,info)
            }

        return context.contentResolver.insert(CONTENT_URI, insertValue) != null
    }

    /**
     * return value is
     *  line num -> line num
     *  error -> -1
     */
    fun remove(pkgName:String, info:String="") : Int {
        return if(info.isEmpty()) context.contentResolver.delete(CONTENT_URI, "$PKG_NAME=?", arrayOf(pkgName))
        else context.contentResolver.delete(CONTENT_URI, "$PKG_NAME=? AND $INFO=?", arrayOf(pkgName,info))
    }

    data class LQItem(
        var id: Int,
        var pkgName: String,
        var info:String
    )

}