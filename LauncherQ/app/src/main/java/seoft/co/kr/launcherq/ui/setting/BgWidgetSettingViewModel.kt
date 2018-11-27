package seoft.co.kr.launcherq.ui.setting

import android.databinding.ObservableField
import android.graphics.Bitmap
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.data.model.BackgroundWidgetInfos
import seoft.co.kr.launcherq.ui.ViewModelHelper

class BgWidgetSettingViewModel(val repo: Repo): ViewModelHelper() {

    val TAG = "BgWidgetSettingViewModel#$#"

    val bgBitmap : ObservableField<Bitmap> by lazy { ObservableField(repo.backgroundRepo.loadBitmap()) }
    val bgwi : ObservableField<BackgroundWidgetInfos> by lazy { ObservableField(repo.preference.getBgWidgetInfos()) }
    val useTime : ObservableField<Boolean> by lazy { ObservableField(repo.preference.getBgTimeUse()) }
    val useDate : ObservableField<Boolean> by lazy { ObservableField(repo.preference.getBgDateUse()) }
    val useAmpm : ObservableField<Boolean> by lazy { ObservableField(repo.preference.getBgAmpmUse()) }
    val useDow : ObservableField<Boolean> by lazy { ObservableField(repo.preference.getBgDowUse()) }
    val useText : ObservableField<Boolean> by lazy { ObservableField(repo.preference.getBgTextUse()) }

    override fun start() {



    }


}