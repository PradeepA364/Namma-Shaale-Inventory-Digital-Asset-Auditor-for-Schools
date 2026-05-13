package com.example.nammashaleinventoryeducation.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.nammashaleinventoryeducation.data.entity.IssueLog
import kotlinx.coroutines.flow.Flow

@Dao
interface IssueLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: IssueLog)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(logs: List<IssueLog>)

    @Update
    suspend fun updateLog(log: IssueLog)

    @Query("SELECT * FROM issue_logs ORDER BY issueDate DESC")
    fun getAllIssues(): Flow<List<IssueLog>>

    @Query("SELECT * FROM issue_logs WHERE repairStatus != 'Completed'")
    fun getPendingRepairs(): Flow<List<IssueLog>>
}
