package com.medo.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

sealed interface StorageKey {
    data object HasSeenWelcome : StorageKey
    data object SearchHistory : StorageKey
}

interface StorageRepository {
    fun getBoolean(key: StorageKey): Flow<Boolean>

    suspend fun setBoolean(key: StorageKey, value: Boolean)

    fun getSearchHistory(): Flow<Set<String>>

    suspend fun addToSearchHistory(value: String)

    suspend fun removeFromSearchHistory(value: String)
}

@Singleton
class DataStoreStorageRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : StorageRepository {

    override fun getBoolean(key: StorageKey): Flow<Boolean> =
        dataStore.data.map { it[booleanPreferencesKey(key.javaClass.name)] ?: false }

    override suspend fun setBoolean(key: StorageKey, value: Boolean) {
        dataStore.edit {
            it[booleanPreferencesKey(key.javaClass.name)] = value
        }
    }

    override fun getSearchHistory(): Flow<Set<String>> =
        dataStore.data.map { it[stringSetPreferencesKey(StorageKey.SearchHistory.javaClass.name)] ?: emptySet() }

    override suspend fun addToSearchHistory(value: String) {
        dataStore.edit {
            val setKey = stringSetPreferencesKey(StorageKey.SearchHistory.javaClass.name)
            val existingSet = it[setKey] ?: LinkedHashSet()
            if (existingSet.contains(value)) return@edit

            it[setKey] = LinkedHashSet<String>().apply {
                addAll(
                    when {
                        existingSet.size >= 5 -> existingSet.drop(1)
                        else -> existingSet
                    }
                )
                add(value)
            }
        }
    }

    override suspend fun removeFromSearchHistory(value: String) {
        dataStore.edit {
            val setKey = stringSetPreferencesKey(StorageKey.SearchHistory.javaClass.name)
            val existingSet = it[setKey] ?: LinkedHashSet()
            it[setKey] = LinkedHashSet<String>().apply {
                addAll(existingSet)
                remove(value)
            }
        }
    }
}