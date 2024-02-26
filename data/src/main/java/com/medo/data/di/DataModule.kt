package com.medo.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.medo.data.repository.DataStoreStorageRepository
import com.medo.data.repository.StorageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences")

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> = context.dataStore

    @Provides
    fun provideStorageRepository(dataStore: DataStore<Preferences>): StorageRepository =
        DataStoreStorageRepository(dataStore)
}