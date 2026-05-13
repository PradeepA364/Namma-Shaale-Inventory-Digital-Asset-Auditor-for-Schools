package com.example.nammashaleinventoryeducation.data.repository

import com.example.nammashaleinventoryeducation.data.dao.AssetDao
import com.example.nammashaleinventoryeducation.data.entity.Asset
import kotlinx.coroutines.flow.Flow

class AssetRepository(private val assetDao: AssetDao) {
    fun getAllAssets(): Flow<List<Asset>> = assetDao.getAllAssets()

    suspend fun getAssetById(id: Int): Asset? = assetDao.getAssetById(id)

    fun getAssetsByCondition(condition: String): Flow<List<Asset>> = assetDao.getAssetsByCondition(condition)

    fun searchAssets(query: String): Flow<List<Asset>> = assetDao.searchAssets(query)

    suspend fun addAsset(asset: Asset) = assetDao.insertAsset(asset)

    suspend fun updateAsset(asset: Asset) = assetDao.updateAsset(asset)

    suspend fun deleteAsset(asset: Asset) = assetDao.deleteAsset(asset)

    fun getAssetCount(): Flow<Int> = assetDao.getAssetCount()

    fun getCountByCondition(condition: String): Flow<Int> = assetDao.getCountByCondition(condition)
}
