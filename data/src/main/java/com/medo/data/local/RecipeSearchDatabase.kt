package com.medo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.medo.data.local.converters.DateConverter
import com.medo.data.local.converters.StringListConverter
import com.medo.data.local.dao.SearchHistoryDao
import com.medo.data.local.dao.SearchResultsDao
import com.medo.data.local.model.Favorite
import com.medo.data.local.model.Ingredient
import com.medo.data.local.model.Recipe
import com.medo.data.local.model.SearchHistory

@Database(
    entities = [
        Recipe::class,
        Ingredient::class,
        Favorite::class,
        SearchHistory::class,
    ],
    version = 1,
)
@TypeConverters(
    StringListConverter::class,
    DateConverter::class,
)
abstract class RecipeSearchDatabase : RoomDatabase() {
    abstract fun searchResultsDao(): SearchResultsDao

    abstract fun searchHistoryDao(): SearchHistoryDao
}