package seoft.co.kr.launcherq.utill

import android.databinding.BindingAdapter
import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.data.model.BackgroundWidgetInfos
import seoft.co.kr.launcherq.data.model.WidgetInfoType
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("bgImg")
fun setBgImg(imageView: ImageView, bitmap: Bitmap) { imageView.setImageBitmap(bitmap) }

@BindingAdapter("bgwi","isUse","type")
fun setBgTime(tv:TextView, bgwi : BackgroundWidgetInfos, isUse:Boolean, type: WidgetInfoType) {

    if(!isUse) {
        tv.visibility = View.GONE
        return
    }

    tv.visibility = View.VISIBLE

    bgwi.Infos[type.get].run {

        tv.let {
            it.textSize = size.toFloat()
            it.layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
                .apply {
                    leftMargin = posX
                    topMargin = posY }
            it.setTextColor(color.toIntColor())

            when(type){
                WidgetInfoType.TIME -> {
                    // etc's default value is 0~23
                    // if use ampm widget hour is 0~12 or not hour is 0~23
                    var rstStr = if(Repo.preference.getBgAmpmUse()) etc.replace("HH","hh") else etc
                    val sdf = SimpleDateFormat(rstStr)
                    it.text = sdf.format(Date())
                }
                WidgetInfoType.AMPM -> {

                    // regex is AM%%PM
                    // example ) 오전%%오후

                    var strs = etc.split("%%")
                    val sdf = SimpleDateFormat("a")
                    val rst = if(sdf.format(Date()) == "AM") strs[0] else strs[1]
                    it.text = rst
                }
                WidgetInfoType.DATE -> {

                }
                WidgetInfoType.DOW -> {

                }
                WidgetInfoType.TEXT -> {

                }

            }

        }

    }

}

