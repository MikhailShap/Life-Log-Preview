package com.lifelog.core.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lifelog.core.data.local.entity.SideEffectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SideEffectDao {
    @Query("SELECT * FROM side_effects ORDER BY date DESC")
    fun getAllSideEffects(): Flow<List<SideEffectEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSideEffect(sideEffect: SideEffectEntity)

    @Delete
    suspend fun deleteSideEffect(sideEffect: SideEffectEntity)
}
