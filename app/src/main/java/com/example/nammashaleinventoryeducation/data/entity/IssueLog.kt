package com.example.nammashaleinventoryeducation.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "issue_logs")
data class IssueLog(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val assetId: Int,
    val issueDescription: String,
    val issueDate: String,
    val repairStatus: String // "Pending", "In Progress", "Completed"
)
