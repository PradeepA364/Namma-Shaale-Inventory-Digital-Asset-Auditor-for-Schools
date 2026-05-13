package com.example.nammashaleinventoryeducation.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "health_logs")
data class HealthLog(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val assetId: Int,
    val status: String,
    val updatedDate: String
)
