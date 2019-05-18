package seoft.co.kr.launcherq.ui.arrange

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.item_arrange_app.view.*
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
 * need to insert pixel value into itemSize param
 */
class ArrangeImageAdapter(val context:Context, val repo: Repo, val qApps: MutableList<QuickApp>, val itemSize:Int, val curDir:Int, val cb:(CallbackArrangeGrid)->Unit) : BaseAdapter() {

    val TAG = "ArrangeImageAdapter#$#"

    override fun getView(pos: Int, view: View?, parent: ViewGroup?): View {

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val item = inflater.inflate(R.layout.item_arrange_app,null)

        if(qApps[pos].hasImg) {

            val imgDir = "${curDir}#${pos}"

            if(repo.imageCacheRepo.containsKey(imgDir)) {
                item.ivIcon.setImageDrawable(repo.imageCacheRepo.getDrawable(imgDir))
            } else {
                // SC.imgDir is saved in BackgroundRepo class
                item.ivIcon.setImageBitmap(BitmapFactory.decodeStream(FileInputStream(File(SC.imgDir,imgDir))))
            }

        }
        else if(qApps[pos].commonApp.isExcept) {

            item.ivIcon.setImageResource(
                CAppException.values().find { it.get == qApps[pos].commonApp.pkgName }?.rss ?: R.drawable.ic_error_orange
            )

        } else {
            when (qApps[pos].type) {
                QuickAppType.EMPTY -> item.ivIcon.setImageResource(R.drawable.ic_dot_black)
                QuickAppType.FOLDER -> item.ivIcon.setImageResource(R.drawable.ic_folder_im)
                QuickAppType.ONE_APP, QuickAppType.TWO_APP -> item.ivIcon.setImageDrawable(App.get.packageManager.getApplicationIcon(qApps[pos].commonApp.pkgName))
                QuickAppType.EXPERT -> item.ivIcon.setImageResource(R.drawable.ic_expert_im)
            }
        }


        item.rlArrangeApp.setOnClickListener { _ ->
            qApps.forEach { it.isPicked = false }
            qApps[pos].isPicked = true
            notifyDataSetChanged()
            cb.invoke(CallbackArrangeGrid(qApps[pos],pos))
        }

        if(qApps[pos].isPicked) item.ivSelect.visibility = View.VISIBLE
        else item.ivSelect.visibility = View.INVISIBLE

        val params = RelativeLayout.LayoutParams(itemSize,itemSize)
        item.ivSelect.layoutParams = params
        item.ivIcon.layoutParams = params

        return item
    }

    override fun getItem(pos: Int) = null
    override fun getItemId(p0: Int): Long = 0
    override fun getCount(): Int = qApps.count()

    data class CallbackArrangeGrid(var quickApp: QuickApp, var pos:Int)

}