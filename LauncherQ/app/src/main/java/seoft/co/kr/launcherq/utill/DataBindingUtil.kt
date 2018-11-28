package seoft.co.kr.launcherq.utill

import android.databinding.BindingAdapter
import android.graphics.Bitmap
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.data.model.BackgroundWidgetInfos
import seoft.co.kr.launcherq.data.model.WidgetInfoType
import java.text.SimpleDateFormat
import java.util.*
import android.view.MotionEvent



@BindingAdapter("bgImg")
fun setBgImg(imageView: ImageView, bitmap: Bitmap) { imageView.setImageBitmap(bitmap) }

@BindingAdapter("bgwi","isUse","type")
fun setBgEtc(tv:TextView, bgwi : BackgroundWidgetInfos, isUse:Boolean, type: WidgetInfoType) {

    if(!isUse) {
        tv.visibility = View.GONE
        return
    }

    tv.visibility = View.VISIBLE

    bgwi.Infos[type.getInt].run {

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
                    val rstStr = if(Repo.preference.getBgAmpmUse()) etc.replace("HH","hh") else etc
                    val sdf = SimpleDateFormat(rstStr)
                    it.text = sdf.format(Date())
                }
                WidgetInfoType.AMPM -> {

                    // regex is AM%%PM
                    // example ) 오전%%오후
                    val strs = etc.split("%%")
                    val sdf = SimpleDateFormat("a")
                    val rst = if(sdf.format(Date()) == "AM") strs[0] else strs[1]
                    it.text = rst
                }
                WidgetInfoType.DATE -> {
                    val sdf = SimpleDateFormat(etc)
                    it.text = sdf.format(Date())
                }
                WidgetInfoType.DOW -> {

                    val strs = etc.split("%%")

                    // etc's example is  일%%월%%화%%수%%목%%금%%토
                    // sun mon tue wed thu fri sat
                    // 1   2   3   4   5   6   7
                    val todayDow = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
                    it.text = strs[todayDow-1]
                }
                WidgetInfoType.TEXT -> {
                    val strs = etc.split("%%")
                    it.text = strs[(0 until strs.size).random()]
                }

            }

        }

    }

}


@BindingAdapter("type","useTime","useAmpm","useDate","useDow","useText")
fun setOnOffButton(bt: Button, type: WidgetInfoType, useTime:Boolean,useAmpm:Boolean,
                   useDate:Boolean,useDow:Boolean,useText:Boolean) {

    val isUse = when(type) {
        WidgetInfoType.TIME -> useTime
        WidgetInfoType.AMPM -> useAmpm
        WidgetInfoType.DATE -> useDate
        WidgetInfoType.DOW -> useDow
        WidgetInfoType.TEXT -> useText
        else -> false
    }

    if (isUse) bt.text = SC.ON_WIDGET
    else bt.text = SC.OFF_WIDGET
}
