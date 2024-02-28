package com.medo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.medo.data.local.converters.StringListConverter
import com.medo.data.local.dao.SearchResultsDao
import com.medo.data.local.model.Ingredient
import com.medo.data.local.model.Recipe

@Database(
    entities = [
        Recipe::class,
        Ingredient::class,
    ],
    version = 2,
)
@TypeConverters(
    StringListConverter::class,
)
abstract class RecipeSearchDatabase : RoomDatabase() {
    abstract fun searchResultsDao(): SearchResultsDao
}