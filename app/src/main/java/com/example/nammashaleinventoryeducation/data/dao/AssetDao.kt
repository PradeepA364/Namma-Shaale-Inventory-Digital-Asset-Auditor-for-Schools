package com.example.nammashaleinventoryeducation.data.dao

import androidx.room.*
import com.example.nammashaleinventoryeducation.data.entity.Asset
import kotlinx.coroutines.flow.Flow

@Dao
interface AssetDao {
    @Query("SELECT * FROM assets")
    fun getAllAssets(): Flow<List<Asset>>

    @Query("SELECT * FROM assets WHERE id = :id")
    suspend fun getAssetById(id: Int): Asset?

    @Query("SELECT * FROM assets WHERE condition = :condition")
    fun getAssetsByCondition(condition: String): Flow<List<Asset>>

    @Query("SELECT * FROM assets WHERE assetName LIKE '%' || :query || '%' OR serialNumber LIKE '%' || :query || '%'")
    fun searchAssets(query: String): Flow<List<Asset>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAsset(asset: Asset)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(assets: List<Asset>)

    @Update
    suspend fun updateAsset(asset: Asset)

    @Delete
    suspend fun deleteAsset(asset: Asset)

    @Query("SELECT COUNT(*) FROM assets")
    fun getAssetCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM assets WHERE condition = :condition")
    fun getCountByCondition(condition: String): Flow<Int>
}
