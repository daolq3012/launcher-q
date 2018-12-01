package seoft.co.kr.launcherq.ui.setting

import android.databinding.ObservableField
import android.graphics.Bitmap
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.data.model.BackgroundWidgetInfos
import seoft.co.kr.launcherq.data.model.WidgetInfoType
import seoft.co.kr.launcherq.ui.MsgType
import seoft.co.kr.launcherq.ui.ViewModelHelper
import seoft.co.kr.launcherq.utill.SC
import seoft.co.kr.launcherq.utill.i

class BgWidgetSettingViewModel(val repo: Repo, val widgetInfoType_: WidgetInfoType): ViewModelHelper() {

    val TAG = "BgWidgetSettingViewModel#$#"

    val bgBitmap : ObservableField<Bitmap> by lazy { ObservableField(repo.backgroundRepo.loadBitmap()) }

    val bgwi : ObservableField<BackgroundWidgetInfos> by lazy { ObservableField(repo.preference.getBgWidgetInfos()) }

    val useTime : ObservableField<Boolean> by lazy { ObservableField(repo.preference.getBgTimeUse()) }
    val useAmpm : ObservableField<Boolean> by lazy { ObservableField(repo.preference.getBgAmpmUse()) }
    val useDate : ObservableField<Boolean> by lazy { ObservableField(repo.preference.getBgDateUse()) }
    val useDow : ObservableField<Boolean> by lazy { ObservableField(repo.preference.getBgDowUse()) }
    val useText : ObservableField<Boolean> by lazy { ObservableField(repo.preference.getBgTextUse()) }

    val initSize : ObservableField<Int> by lazy { ObservableField(repo.preference.getBgWidgetInfos().Infos[widgetInfoType_.getInt].size) }

    val widgetInfoType : ObservableField<WidgetInfoType> by lazy {
        ObservableField(widgetInfoType_)
    }

    override fun start() {



    }

    fun savePos(x:Int,y:Int){
        bgwi.get()?.run {
            Infos[widgetInfoType_.getInt].posX = x
            Infos[widgetInfoType_.getInt].posY = y

            repo.preference.setBgWidgetInfos(this)
            bgwi.set(this)
        }
    }

    fun saveColor(colorCode: String) {
        bgwi.get()?.copy()?.run {
            Infos[widgetInfoType_.getInt].color = colorCode
            repo.preference.setBgWidgetInfos(this)
            bgwi.set(this)
        }
    }

    fun clickColorBt() {
        bgwi.get()?.run {
            toActMsg(MsgType.PICK_COLOR,Infos[widgetInfoType_.getInt].color)
        }
    }

    fun clickEtcBt() {
        toActMsg(MsgType.OPEN_INPUT_DIALOG,
            bgwi.get()?.run {
                Infos[widgetInfoType_.getInt].etc
            })
    }

    fun clickResetBt() {
        bgwi.get()?.copy()?.run {
            Infos[widgetInfoType_.getInt].posX = (100 until 200).random()
            Infos[widgetInfoType_.getInt].posY = (100 until 500).random()
            val randSize = (10 until 40).random()
            initSize.set(randSize)
            Infos[widgetInfoType_.getInt].size = randSize

            repo.preference.setBgWidgetInfos(this)
            bgwi.set(this)
        }
    }

    fun clickOnOffBt(v:View) {

        val bt = v as Button
        val b = (bt.text != SC.ON_WIDGET)

        Repo.preference.run {
            when(widgetInfoType.get()) {
                WidgetInfoType.TIME -> {
                    useTime.set(b)
                    setBgTimeUse(b)
                }
                WidgetInfoType.AMPM -> {
                    useAmpm.set(b)
                    setBgAmpmUse(b)
                }
                WidgetInfoType.DATE -> {
                    useDate.set(b)
                    setBgDateUse(b)
                }
                WidgetInfoType.DOW -> {
                    useDow.set(b)
                    setBgDowUse(b)
                }
                WidgetInfoType.TEXT -> {
                    useText.set(b)
                    setBgTextUse(b)
                }
            }
        }
    }

    fun changeSizeSb(sb:SeekBar,fSize:Int,b:Boolean){

        bgwi.get()?.copy()?.run {
            Infos[widgetInfoType_.getInt].size = fSize
            repo.preference.setBgWidgetInfos(this)
            bgwi.set(this)
        }

    }

    fun saveEtc(str:String) {
        bgwi.get()?.copy()?.run {
            Infos[widgetInfoType_.getInt].etc = str
            repo.preference.setBgWidgetInfos(this)
            bgwi.set(this)
        }
    }

    fun saveWhenOnPause() {
        bgwi.get()?.copy()?.run {
            repo.preference.setBgWidgetInfos(this)
        }
        SC.needResetBgSetting = true
    }

}