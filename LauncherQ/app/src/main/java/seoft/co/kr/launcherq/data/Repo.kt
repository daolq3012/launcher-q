package seoft.co.kr.launcherq.data

import android.arch.persistence.room.Room
import seoft.co.kr.launcherq.data.local.BackgroundRepo
import seoft.co.kr.launcherq.data.local.LocalDB
import seoft.co.kr.launcherq.data.local.PreferenceRepo
import seoft.co.kr.launcherq.utill.App

object Repo{
    val preference = PreferenceRepo()
    val backgroundRepo = BackgroundRepo()
    val localDBRepo = Room.databaseBuilder(App.get.applicationContext, LocalDB::class.java, "command.db")
        .allowMainThreadQueries()
        .build()
}