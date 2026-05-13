package com.example.nammashaleinventoryeducation.data.repository

import com.example.nammashaleinventoryeducation.data.dao.HealthLogDao
import com.example.nammashaleinventoryeducation.data.entity.HealthLog
import kotlinx.coroutines.flow.Flow

class HealthLogRepository(private val healthLogDao: HealthLogDao) {
    suspend fun insertLog(log: HealthLog) = healthLogDao.insertLog(log)

    fun getLogsForAsset(assetId: Int): Flow<List<HealthLog>> = healthLogDao.getLogsForAsset(assetId)
}
