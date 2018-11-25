package seoft.co.kr.launcherq.ui.main

import android.databinding.BindingAdapter
import android.widget.ImageView
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.utill.i

@BindingAdapter(value=arrayOf("bgImg"))
fun setBgImg(imageView: ImageView,b:Boolean) {

    b.toString().i()

    if(!b) return
    imageView.setImageBitmap(Repo.backgroundRepo.loadBitmap())
}
