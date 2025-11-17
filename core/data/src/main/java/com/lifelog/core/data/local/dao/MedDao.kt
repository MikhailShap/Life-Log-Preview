package com.lifelog.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lifelog.core.data.local.entity.MedEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MedDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(med: MedEntity)

    @Query("SELECT * FROM meds ORDER BY name ASC")
    fun getAllMeds(): Flow<List<MedEntity>>

    @Query("DELETE FROM meds WHERE id = :id")
    suspend fun deleteMedById(id: Long)
}
