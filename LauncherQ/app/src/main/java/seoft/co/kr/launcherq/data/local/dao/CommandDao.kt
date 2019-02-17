package seoft.co.kr.launcherq.data.local.dao

import android.arch.persistence.room.*
import android.database.Cursor
import seoft.co.kr.launcherq.data.model.Command

@Dao
interface CommandDao{

    @Query("SELECT COUNT(*) FROM ${Command.TABLE_NAME}" )
    fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cmd: Command): Long

    @Insert
    fun insertAll(cmds: Array<Command>): LongArray

    @Query("SELECT * FROM ${Command.TABLE_NAME}")
    fun selectAll(): Cursor

    @Query("SELECT * FROM ${Command.TABLE_NAME} WHERE ${Command.COLUMN_ID} = :id")
    fun selectById(id: Long): Cursor

    @Query("DELETE FROM ${Command.TABLE_NAME} WHERE ${Command.COLUMN_ID} = :id")
    fun deleteById(id: Long): Int

    @Update
    fun update(cmd: Command): Int
}