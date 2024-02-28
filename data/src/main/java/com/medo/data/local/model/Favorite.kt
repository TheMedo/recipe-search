package com.medo.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class Favorite(
    @PrimaryKey
    val recipeUri: String,
    val isFavorite: Boolean = false,
)