package com.medo.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

sealed interface StorageKey {
    data object HasSeenWelcome : StorageKey
}

interface StorageRepository {
    fun getBoolean(key: StorageKey): Flow<Boolean>

    suspend fun setBoolean(key: StorageKey, value: Boolean)
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
}