package seoft.co.kr.launcherq.data.local

import android.graphics.drawable.Drawable

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





}