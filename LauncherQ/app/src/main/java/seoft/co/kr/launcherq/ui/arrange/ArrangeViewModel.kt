package seoft.co.kr.launcherq.ui.arrange

import android.arch.lifecycle.MutableLiveData
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.data.model.QuickApp
import seoft.co.kr.launcherq.ui.ViewModelHelper


class ArrangeViewModel(val repo: Repo): ViewModelHelper() {

    val TAG = "ArrangeViewModel#$#"

    var gridCnt = 3
    var liveDataApps = MutableLiveData<MutableList<QuickApp>>()
    lateinit var pkgName: String
    lateinit var label: String
    lateinit var detailName: String

    override fun start() {

    }

    fun refreshApp(dir:Int) {
        gridCnt = repo.preference.getGridCount()
        liveDataApps.value = repo.preference.getQuickApps(dir)
    }

    fun clickTop() { refreshApp(0) }
    fun clickRight() { refreshApp(1) }
    fun clickBottom() { refreshApp(2) }
    fun clickLeft() { refreshApp(3) }




}