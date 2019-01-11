package seoft.co.kr.launcherq.ui.arrange

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.data.model.CommonApp
import seoft.co.kr.launcherq.data.model.QuickApp
import seoft.co.kr.launcherq.data.model.QuickAppType
import seoft.co.kr.launcherq.ui.ViewModelHelper
import seoft.co.kr.launcherq.utill.i


class ArrangeViewModel(val repo: Repo): ViewModelHelper() {

    val TAG = "ArrangeViewModel#$#"

    companion object {
        val NONE_PICK = "NONE_PICK"
    }
    val NONE_PICKED_APP = QuickApp(CommonApp(NONE_PICK,"","",false), QuickAppType.EMPTY, emptyArray() )

    var gridCnt = 3
    var liveDataApps = MutableLiveData<MutableList<QuickApp>>()

    var dir = 0
    val pickedApp = ObservableField( NONE_PICKED_APP)

    var insertingApp: CommonApp? = null

    override fun start() {
    }

    fun refreshAppGrid() {
        pickedApp.set(NONE_PICKED_APP)
        gridCnt = repo.preference.getGridCount()
        liveDataApps.value = repo.preference.getQuickApps(dir)
    }

    fun setPickedApp(quickApp: QuickApp,pos:Int){
        "setPickedApp".i(TAG)
        quickApp.toString().i(TAG)
        pos.toString().i(TAG)

        insertingApp?.let {
            val changedLiveDataApps = liveDataApps.value!!
            changedLiveDataApps[pos] = QuickApp(insertingApp!!, QuickAppType.ONE_APP, emptyArray() )
            insertingApp = null
            repo.preference.setQuickApps(changedLiveDataApps ,dir)
            refreshAppGrid()
        } ?: let {
            pickedApp.set(quickApp)
        }
    }

    fun clickAdd(){
        "clickAdd".i()
    }

    fun clickDelete(){
        "clickDelete".i()
    }

    fun clickMove(){
        "clickMove".i()
    }

    fun clickFolder(){
        "clickFolder".i()
    }

    fun clickTwoDepth(){
        "clickTwoDepth".i()
    }

    fun clickExpert(){
        "clickExpert".i()
    }


    fun clickTop() { dir = 0
        refreshAppGrid() }

    fun clickRight() { dir = 1
        refreshAppGrid() }

    fun clickBottom() { dir = 2
        refreshAppGrid() }

    fun clickLeft() { dir = 3
        refreshAppGrid() }

    enum class ArrangeBottoms{
        ADD,
        DELETE,
        MOVE,
        FOLDER,
        TWO_DEPTH,
        EXPERT
    }


}