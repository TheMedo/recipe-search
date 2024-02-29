package com.medo.data.repository

import com.medo.data.local.dao.SearchHistoryDao
import com.medo.data.local.model.SearchHistory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface SearchHistoryRepository {
    fun getSearchHistory(): Flow<List<SearchHistory>>

    suspend fun addSearchHistory(query: String)

    suspend fun removeSearchHistory(searchHistory: SearchHistory)
}

class RoomSearchHistoryRepository @Inject constructor(
    private val local: SearchHistoryDao,
) : SearchHistoryRepository {

    override fun getSearchHistory(): Flow<List<SearchHistory>> = local.getSearchHistory()

    override suspend fun addSearchHistory(query: String) = local.updateSearchHistory(SearchHistory(query = query))

    override suspend fun removeSearchHistory(searchHistory: SearchHistory) = local.deleteSearchHistory(searchHistory)
}