package com.example.nammashaleinventoryeducation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.nammashaleinventoryeducation.data.database.AppDatabase
import com.example.nammashaleinventoryeducation.data.entity.Asset
import com.example.nammashaleinventoryeducation.data.repository.AssetRepository
import com.example.nammashaleinventoryeducation.utils.Constants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AssetViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: AssetRepository

    init {
        val db = AppDatabase.getDatabase(application)
        repository = AssetRepository(db.assetDao())
    }

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _selectedFilter = MutableStateFlow("All")
    val selectedFilter: StateFlow<String> = _selectedFilter

    private val _selectedAsset = MutableStateFlow<Asset?>(null)
    val selectedAsset: StateFlow<Asset?> = _selectedAsset

    @OptIn(ExperimentalCoroutinesApi::class)
    val filteredAssets: StateFlow<List<Asset>> = combine(
        _searchQuery, _selectedFilter
    ) { query, filter -> Pair(query, filter) }
        .flatMapLatest { (query, filter) ->
            when {
                query.isNotBlank() -> repository.searchAssets(query)
                filter != "All" -> repository.getAssetsByCondition(filter)
                else -> repository.getAllAssets()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setSearchQuery(query: String) { _searchQuery.value = query }
    fun setFilter(filter: String) { _selectedFilter.value = filter }

    fun loadAsset(id: Int) {
        viewModelScope.launch {
            _selectedAsset.value = repository.getAssetById(id)
        }
    }

    fun addAsset(asset: Asset) {
        viewModelScope.launch { repository.addAsset(asset) }
    }

    fun updateAsset(asset: Asset) {
        viewModelScope.launch { repository.updateAsset(asset) }
    }

    fun deleteAsset(asset: Asset) {
        viewModelScope.launch { repository.deleteAsset(asset) }
    }
}
