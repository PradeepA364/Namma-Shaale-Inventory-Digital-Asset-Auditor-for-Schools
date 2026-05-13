package com.example.nammashaleinventoryeducation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.nammashaleinventoryeducation.data.database.AppDatabase
import com.example.nammashaleinventoryeducation.data.entity.Asset
import com.example.nammashaleinventoryeducation.data.repository.AssetRepository
import com.example.nammashaleinventoryeducation.data.repository.IssueLogRepository
import com.example.nammashaleinventoryeducation.utils.Constants
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File

class ReportsViewModel(application: Application) : AndroidViewModel(application) {
    private val assetRepository: AssetRepository
    private val issueRepository: IssueLogRepository

    init {
        val db = AppDatabase.getDatabase(application)
        assetRepository = AssetRepository(db.assetDao())
        issueRepository = IssueLogRepository(db.issueLogDao())
    }

    val allAssets: StateFlow<List<Asset>> = assetRepository.getAllAssets()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalCount: Flow<Int> = assetRepository.getAssetCount()
    val workingCount: Flow<Int> = assetRepository.getCountByCondition(Constants.CONDITION_WORKING)
    val repairCount: Flow<Int> = assetRepository.getCountByCondition(Constants.CONDITION_NEEDS_REPAIR)
    val brokenCount: Flow<Int> = assetRepository.getCountByCondition(Constants.CONDITION_BROKEN)

    val pendingIssues = issueRepository.getPendingRepairs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categoryDistribution: StateFlow<Map<String, Int>> = allAssets
        .map { assets -> assets.groupBy { it.category }.mapValues { it.value.size } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    private val _pdfFile = MutableStateFlow<File?>(null)
    val pdfFile: StateFlow<File?> = _pdfFile

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating

    fun generateReport() {
        if (_isGenerating.value) return
        
        viewModelScope.launch {
            _isGenerating.value = true
            try {
                val context = getApplication<Application>()
                val assets = allAssets.value
                val total = assets.size
                val working = assets.count { it.condition == Constants.CONDITION_WORKING }
                val repair = assets.count { it.condition == Constants.CONDITION_NEEDS_REPAIR }
                val broken = assets.count { it.condition == Constants.CONDITION_BROKEN }

                val file = com.example.nammashaleinventoryeducation.utils.PdfGenerator.generateAssetReport(
                    context,
                    assets,
                    total,
                    working,
                    repair,
                    broken
                )
                _pdfFile.value = file
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isGenerating.value = false
            }
        }
    }

    fun resetPdfFile() {
        _pdfFile.value = null
    }

    fun getCategoryDistribution(): Map<String, Int> {
        return allAssets.value.groupBy { it.category }.mapValues { it.value.size }
    }
}
