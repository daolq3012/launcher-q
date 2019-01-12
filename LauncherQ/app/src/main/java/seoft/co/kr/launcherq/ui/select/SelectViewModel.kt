package seoft.co.kr.launcherq.ui.select

import android.arch.lifecycle.MutableLiveData
import seoft.co.kr.launcherq.data.Repo
import seoft.co.kr.launcherq.data.model.CommonApp
import seoft.co.kr.launcherq.ui.ViewModelHelper
import seoft.co.kr.launcherq.utill.InstalledAppUtil

class SelectViewModel(val repo: Repo): ViewModelHelper() {

    val TAG = "SelectViewModel#$#"

    var liveDataCommonApps = MutableLiveData<List<CommonApp>>()

    override fun start() {
        val instApps = InstalledAppUtil().getInstalledApps()
        liveDataCommonApps.value = instApps
    }


}