package seoft.co.kr.launcherq.data

import seoft.co.kr.launcherq.data.local.BackgroundRepo
import seoft.co.kr.launcherq.data.local.PreferenceRepo

object Repo{
    val preference = PreferenceRepo()
    val backgroundRepo = BackgroundRepo()

}