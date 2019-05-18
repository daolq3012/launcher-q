package seoft.co.kr.launcherq.ui.main

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.item_main_app.view.*
import seoft.co.kr.launcherq.R
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.data.model.CAppException
import seoft.co.kr.launcherq.data.model.QuickApp
import seoft.co.kr.launcherq.data.model.QuickAppType
import seoft.co.kr.launcherq.utill.App
import seoft.co.kr.launcherq.utill.SC
import java.io.File
import java.io.FileInputStream

/**
 * need to insert pixel value into gridItemSize param
 */
class MainGridAdapter(val context: Context, val repo: Repo, val qApps: MutableList<QuickApp>, val gridItemSize:Int, val curDir:Int, val cb:(CallbackMainGrid)->Unit) : BaseAdapter() {

    val TAG = "MainGridAdapter#$#"

    override fun getView(pos: Int, view: View?, parent: ViewGroup?): View {

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val item = inflater.inflate(R.layout.item_main_app,null)

        val tmpPkgName = qApps[pos].commonApp.pkgName

        if(qApps[pos].hasImg) {
            val imgDir = "${curDir}#${pos}"

            if(repo.imageCacheRepo.containsKey(imgDir)) {
                item.ivIcon.setImageDrawable(repo.imageCacheRepo.getDrawable(imgDir))
            } else {
                // SC.imgDir is saved in BackgroundRepo class
                item.ivIcon.setImageBitmap(BitmapFactory.decodeStream(FileInputStream(File(SC.imgDir,imgDir))))
            }
        } else if(qApps[pos].commonApp.isExcept) {

            item.ivIcon.setImageResource(
                CAppException.values().find { it.get == tmpPkgName }?.rss ?: R.drawable.ic_error_orange
            )
        } else {
            when(qApps[pos].type) {
                QuickAppType.EMPTY -> item.ivIcon.setImageResource(R.drawable.dot_img)
                QuickAppType.FOLDER -> item.ivIcon.setImageResource(R.drawable.ic_folder_im)
                QuickAppType.ONE_APP, QuickAppType.TWO_APP -> {
                    item.ivIcon.setImageDrawable(
                        if(repo.imageCacheRepo.containsKey(tmpPkgName)) repo.imageCacheRepo.getDrawable(tmpPkgName)
                        else App.get.packageManager.getApplicationIcon(tmpPkgName))
                }
                QuickAppType.EXPERT -> item.ivIcon.setImageResource(R.drawable.ic_expert_im)
            }
        }


        val params = RelativeLayout.LayoutParams(gridItemSize, gridItemSize)
        item.ivIcon.layoutParams = params

        item.ivIcon.setOnClickListener { _ -> cb.invoke(CallbackMainGrid(qApps[pos],false)) }
        item.ivIcon.setOnLongClickListener { _ -> cb.invoke(CallbackMainGrid(qApps[pos],true))
        true }

        return item
    }

    override fun getItem(pos: Int) = null
    override fun getItemId(p0: Int): Long = 0
    override fun getCount(): Int = qApps.count()

}


data class CallbackMainGrid(var quickApp: QuickApp, var isLongClick:Boolean)
