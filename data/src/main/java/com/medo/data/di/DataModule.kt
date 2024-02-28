package com.medo.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.medo.data.BuildConfig
import com.medo.data.local.RecipeSearchDatabase
import com.medo.data.local.dao.SearchResultsDao
import com.medo.data.remote.service.ApiService
import com.medo.data.repository.DataStoreStorageRepository
import com.medo.data.repository.EdamamRecipeRepository
import com.medo.data.repository.RecipeRepository
import com.medo.data.repository.StorageRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences")

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> = context.dataStore

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BASIC)
        })
        .addInterceptor { chain ->
            val url = chain
                .request()
                .url
                .newBuilder()
                .addQueryParameter("app_id", BuildConfig.EDAMAM_API_ID)
                .addQueryParameter("app_key", BuildConfig.EDAMAM_API_KEY)
                .addQueryParameter("type", "public")
                .build()
            chain.proceed(chain.request().newBuilder().url(url).build())
        }
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("https://api.edamam.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): RecipeSearchDatabase = Room
        .databaseBuilder(
            context = context,
            klass = RecipeSearchDatabase::class.java,
            name = "database"
        )
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideSearchResultsDao(database: RecipeSearchDatabase): SearchResultsDao = database.searchResultsDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModuleBinds {
    @Binds
    @Singleton
    abstract fun bindStorageRepository(repository: DataStoreStorageRepository): StorageRepository

    @Binds
    @Singleton
    abstract fun bindRecipeRepository(repository: EdamamRecipeRepository): RecipeRepository
}