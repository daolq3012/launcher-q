package seoft.co.kr.launcherq.ui.setting

import android.databinding.ObservableField
import android.graphics.Bitmap
import android.view.View
import android.widget.Button
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.data.model.BackgroundWidgetInfos
import seoft.co.kr.launcherq.data.model.WidgetInfoType
import seoft.co.kr.launcherq.ui.ViewModelHelper
import seoft.co.kr.launcherq.utill.SC

class BgWidgetSettingViewModel(val repo: Repo, val widgetInfoType_: WidgetInfoType): ViewModelHelper() {

    val TAG = "BgWidgetSettingViewModel#$#"

    val bgBitmap : ObservableField<Bitmap> by lazy { ObservableField(repo.backgroundRepo.loadBitmap()) }
    val bgwi : ObservableField<BackgroundWidgetInfos> by lazy { ObservableField(repo.preference.getBgWidgetInfos()) }
    val useTime : ObservableField<Boolean> by lazy { ObservableField(repo.preference.getBgTimeUse()) }
    val useAmpm : ObservableField<Boolean> by lazy { ObservableField(repo.preference.getBgAmpmUse()) }
    val useDate : ObservableField<Boolean> by lazy { ObservableField(repo.preference.getBgDateUse()) }
    val useDow : ObservableField<Boolean> by lazy { ObservableField(repo.preference.getBgDowUse()) }
    val useText : ObservableField<Boolean> by lazy { ObservableField(repo.preference.getBgTextUse()) }

    val widgetInfoType : ObservableField<WidgetInfoType> by lazy {
        ObservableField(widgetInfoType_)
    }

    override fun start() {



    }

    fun clickOnOffBt(v:View) {

        val bt = v as Button
        val b = (bt.text != SC.ON_WIDGET)

        when(widgetInfoType.get()) {
            WidgetInfoType.TIME -> {
                useTime.set(b)
                Repo.preference.setBgTimeUse(b)
            }
            WidgetInfoType.AMPM -> {
                useAmpm.set(b)
                Repo.preference.setBgAmpmUse(b)
            }
            WidgetInfoType.DATE -> {
                useDate.set(b)
                Repo.preference.setBgDateUse(b)
            }
            WidgetInfoType.DOW -> {
                useDow.set(b)
                Repo.preference.setBgDowUse(b)
            }
            WidgetInfoType.TEXT -> {
                useText.set(b)
                Repo.preference.setBgTextUse(b)
            }
        }



    }


}