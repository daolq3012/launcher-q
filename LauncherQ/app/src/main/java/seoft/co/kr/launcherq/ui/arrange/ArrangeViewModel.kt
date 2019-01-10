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

    var gridCnt = 3
    var liveDataApps = MutableLiveData<MutableList<QuickApp>>()

    var dir = 0

    val pickedApp = ObservableField(QuickApp(CommonApp("","","",false), QuickAppType.EMPTY, emptyArray() ) )

    var insertingApp: CommonApp? = null

    override fun start() {
    }

    fun refreshAppGrid() {
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

    fun clickTop() {
        dir = 0
        refreshAppGrid()
    }

    fun clickRight() {
        dir = 1
        refreshAppGrid()
    }

    fun clickBottom() {
        dir = 2
        refreshAppGrid()
    }

    fun clickLeft() {
        dir = 3
        refreshAppGrid()
    }




}