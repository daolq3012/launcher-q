package seoft.co.kr.launcherq.data.local

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.utill.App
import seoft.co.kr.launcherq.utill.SC
import java.io.File
import java.io.FileInputStream

class ImageCacheRepo{

    val TAG = "ImageCacheRepo#$#"

    companion object {
        val cacheOfImages = mutableMapOf<String, Drawable>()
    }

    fun saveCache(key:String,drawable: Drawable) {
        cacheOfImages[key] = drawable
    }

    fun removeCache(key:String) {
        cacheOfImages.remove(key)
    }

    fun removeAll(){
        cacheOfImages.forEach { cacheOfImages.remove(it.key) }
    }

    fun containsKey(key:String):Boolean {
        return cacheOfImages.containsKey(key)
    }

    fun getDrawable(key:String):Drawable{
        return cacheOfImages[key]!!
    }

    /**
     * dependency from Repo object class
     */
    fun setAll(){

        for(dir in 0 until 4) {
            val qApps = Repo.preference.getQuickApps(dir)
            for ( pos in 0 until 16) {
                val curDir = "${dir}#${pos}"
                if(qApps[pos].hasImg) {
                    val bitmap = BitmapFactory.decodeStream(FileInputStream(File(SC.imgDir,curDir)))
                    cacheOfImages[curDir] = BitmapDrawable(App.get.resources, bitmap)
                }
            }
        }



    }






}