package seoft.co.kr.launcherq.ui.main

import android.databinding.BindingAdapter
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.utill.i

@BindingAdapter("bgImg")
fun setBgImg(imageView: ImageView,b:Boolean) {

    b.toString().i()

    if(!b) return
    imageView.setImageBitmap(Repo.backgroundRepo.loadBitmap())
}

