package seoft.co.kr.launcherq.data.local

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.utill.App
import seoft.co.kr.launcherq.utill.SC.Companion.IMAGE_DIR_NAME
import seoft.co.kr.launcherq.utill.SC.Companion.IMAGE_FILE_NAME
import seoft.co.kr.launcherq.utill.i
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class BackgroundRepo{

    val TAG = "BackgroundRepo#$#"

    fun saveBitmap(bitmap: Bitmap,context:Context) {

        val cw = ContextWrapper(context)
        val dir = cw.getDir(IMAGE_DIR_NAME, Context.MODE_PRIVATE)
        val myPath = File(dir, IMAGE_FILE_NAME)

        val fos = FileOutputStream(myPath)
        bitmap.compress(Bitmap.CompressFormat.PNG,100,fos)

        Repo.preference.setBgImageBitmapPath(dir.absolutePath)
    }

    fun loadBitmap(): Bitmap {

        var path = Repo.preference.getBgImageBitmapPath()

        // if first access, set default_bg.png
        if(path.isNullOrBlank()) {
            val bitImg = BitmapFactory.decodeResource(App.get.resources, R.drawable.default_bg)
            saveBitmap(bitImg, App.get.applicationContext)
            path = Repo.preference.getBgImageBitmapPath()
        }

        val f = File(path, IMAGE_FILE_NAME)
        return BitmapFactory.decodeStream(FileInputStream(f))
    }

}