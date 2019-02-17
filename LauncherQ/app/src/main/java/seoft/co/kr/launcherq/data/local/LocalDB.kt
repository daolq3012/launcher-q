package seoft.co.kr.launcherq.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import seoft.co.kr.launcherq.data.local.dao.CommandDao
import seoft.co.kr.launcherq.data.model.Command

@Database(entities = arrayOf(Command::class), version = 1)
abstract class LocalDB : RoomDatabase() {
    abstract fun commandDao(): CommandDao
}