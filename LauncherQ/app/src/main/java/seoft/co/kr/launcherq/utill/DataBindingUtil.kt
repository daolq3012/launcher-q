package seoft.co.kr.launcherq.utill

import android.databinding.BindingAdapter
import android.graphics.Bitmap
import android.widget.ImageView
import android.widget.TextView
import seoft.co.kr.launcherq.data.model.BackgroundWidgetInfos
import seoft.co.kr.launcherq.data.model.WidgetInfoType

@BindingAdapter("bgImg")
fun setBgImg(imageView: ImageView, bitmap: Bitmap) { imageView.setImageBitmap(bitmap) }

@BindingAdapter("bgTime","isUse")
fun setBgTime(tv:TextView, bgwi : BackgroundWidgetInfos, b:Boolean) {

    tv.text = "${b.toString()} ${bgwi.Infos[WidgetInfoType.TIME.type]}"


}