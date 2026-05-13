package com.example.nammashaleinventoryeducation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.nammashaleinventoryeducation.data.database.AppDatabase
import com.example.nammashaleinventoryeducation.data.repository.AssetRepository
import com.example.nammashaleinventoryeducation.utils.Constants
import com.example.nammashaleinventoryeducation.utils.DateUtils
import com.example.nammashaleinventoryeducation.utils.SessionManager
import kotlinx.coroutines.flow.Flow

class DashboardViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: AssetRepository
    val sessionManager: SessionManager

    init {
        val db = AppDatabase.getDatabase(application)
        repository = AssetRepository(db.assetDao())
        sessionManager = SessionManager(application)
    }

    val totalAssets: Flow<Int> = repository.getAssetCount()
    val workingCount: Flow<Int> = repository.getCountByCondition(Constants.CONDITION_WORKING)
    val repairCount: Flow<Int> = repository.getCountByCondition(Constants.CONDITION_NEEDS_REPAIR)
    val brokenCount: Flow<Int> = repository.getCountByCondition(Constants.CONDITION_BROKEN)

    fun getGreeting(): String = DateUtils.getGreeting()
    fun getUserName(): String = sessionManager.getUserName()
}
