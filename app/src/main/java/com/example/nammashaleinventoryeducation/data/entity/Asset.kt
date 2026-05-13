package com.example.nammashaleinventoryeducation.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assets")
data class Asset(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val assetName: String,
    val serialNumber: String,
    val category: String,
    val condition: String, // "Working", "Needs Repair", "Broken"
    val imagePath: String? = null,
    val notes: String? = null,
    val purchaseDate: String // stored as "yyyy-MM-dd"
)
