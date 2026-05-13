package com.example.nammashaleinventoryeducation.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nammashaleinventoryeducation.data.entity.HealthLog
import kotlinx.coroutines.flow.Flow

@Dao
interface HealthLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: HealthLog)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(logs: List<HealthLog>)

    @Query("SELECT * FROM health_logs WHERE assetId = :assetId ORDER BY updatedDate DESC")
    fun getLogsForAsset(assetId: Int): Flow<List<HealthLog>>
}
