package com.example.nammashaleinventoryeducation.data.repository

import com.example.nammashaleinventoryeducation.data.dao.IssueLogDao
import com.example.nammashaleinventoryeducation.data.entity.IssueLog
import kotlinx.coroutines.flow.Flow

class IssueLogRepository(private val issueLogDao: IssueLogDao) {
    suspend fun insertLog(log: IssueLog) = issueLogDao.insertLog(log)

    suspend fun updateLog(log: IssueLog) = issueLogDao.updateLog(log)

    fun getAllIssues(): Flow<List<IssueLog>> = issueLogDao.getAllIssues()

    fun getPendingRepairs(): Flow<List<IssueLog>> = issueLogDao.getPendingRepairs()
}
