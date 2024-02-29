package com.medo.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.medo.data.local.model.SearchHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {
    @Query("SELECT * FROM search_histories ORDER BY `timestamp` DESC LIMIT 5")
    fun getSearchHistory(): Flow<List<SearchHistory>>

    @Upsert
    suspend fun updateSearchHistory(searchHistory: SearchHistory)

    @Delete
    suspend fun deleteSearchHistory(searchHistory: SearchHistory)
}