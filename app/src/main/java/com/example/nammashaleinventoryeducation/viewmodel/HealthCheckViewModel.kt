package com.example.nammashaleinventoryeducation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.nammashaleinventoryeducation.data.database.AppDatabase
import com.example.nammashaleinventoryeducation.data.entity.Asset
import com.example.nammashaleinventoryeducation.data.entity.HealthLog
import com.example.nammashaleinventoryeducation.data.repository.AssetRepository
import com.example.nammashaleinventoryeducation.data.repository.HealthLogRepository
import com.example.nammashaleinventoryeducation.utils.DateUtils
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class HealthCheckItem(
    val asset: Asset,
    var selectedStatus: String? = null
)

class HealthCheckViewModel(application: Application) : AndroidViewModel(application) {
    private val assetRepository: AssetRepository
    private val healthLogRepository: HealthLogRepository

    init {
        val db = AppDatabase.getDatabase(application)
        assetRepository = AssetRepository(db.assetDao())
        healthLogRepository = HealthLogRepository(db.healthLogDao())
    }

    private val _healthCheckItems = MutableStateFlow<List<HealthCheckItem>>(emptyList())
    val healthCheckItems: StateFlow<List<HealthCheckItem>> = _healthCheckItems

    private val _isSubmitted = MutableStateFlow(false)
    val isSubmitted: StateFlow<Boolean> = _isSubmitted

    val updatedCount: StateFlow<Int> = _healthCheckItems.map { items ->
        items.count { it.selectedStatus != null }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalCount: StateFlow<Int> = _healthCheckItems.map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    init {
        loadAssets()
    }

    private fun loadAssets() {
        viewModelScope.launch {
            assetRepository.getAllAssets().collect { assets ->
                _healthCheckItems.value = assets.map { HealthCheckItem(it) }
            }
        }
    }

    fun updateStatus(assetId: Int, status: String) {
        _healthCheckItems.value = _healthCheckItems.value.map { item ->
            if (item.asset.id == assetId) item.copy(selectedStatus = status) else item
        }
    }

    fun submitHealthCheck() {
        viewModelScope.launch {
            val today = DateUtils.getCurrentDate()
            _healthCheckItems.value.forEach { item ->
                item.selectedStatus?.let { status ->
                    healthLogRepository.insertLog(
                        HealthLog(assetId = item.asset.id, status = status, updatedDate = today)
                    )
                    assetRepository.updateAsset(item.asset.copy(condition = status))
                }
            }
            _isSubmitted.value = true
        }
    }

    fun getLogsForAsset(assetId: Int): Flow<List<HealthLog>> =
        healthLogRepository.getLogsForAsset(assetId)

    fun resetSubmission() { _isSubmitted.value = false }
}
