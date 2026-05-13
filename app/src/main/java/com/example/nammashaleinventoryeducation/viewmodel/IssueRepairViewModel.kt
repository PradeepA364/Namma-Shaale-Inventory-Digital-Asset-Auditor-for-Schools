package com.example.nammashaleinventoryeducation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.nammashaleinventoryeducation.data.database.AppDatabase
import com.example.nammashaleinventoryeducation.data.entity.Asset
import com.example.nammashaleinventoryeducation.data.entity.IssueLog
import com.example.nammashaleinventoryeducation.data.repository.AssetRepository
import com.example.nammashaleinventoryeducation.data.repository.IssueLogRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class IssueRepairViewModel(application: Application) : AndroidViewModel(application) {
    private val assetRepository: AssetRepository
    private val issueRepository: IssueLogRepository

    init {
        val db = AppDatabase.getDatabase(application)
        assetRepository = AssetRepository(db.assetDao())
        issueRepository = IssueLogRepository(db.issueLogDao())
    }

    val allIssues: StateFlow<List<IssueLog>> = issueRepository.getAllIssues()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pendingRepairs: StateFlow<List<IssueLog>> = issueRepository.getPendingRepairs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allAssets: StateFlow<List<Asset>> = assetRepository.getAllAssets()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addIssue(issue: IssueLog) {
        viewModelScope.launch { issueRepository.insertLog(issue) }
    }

    fun updateRepairStatus(issue: IssueLog, status: String) {
        viewModelScope.launch { issueRepository.updateLog(issue.copy(repairStatus = status)) }
    }

    fun getAssetName(assetId: Int): String {
        return allAssets.value.find { it.id == assetId }?.assetName ?: "Unknown Asset"
    }
}
