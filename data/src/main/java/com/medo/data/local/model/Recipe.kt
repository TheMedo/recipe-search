package com.medo.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey
    val uri: String,
    val index: Int = 0,
    val title: String? = null,
    val image: String? = null,
    val shareAs: String? = null,
    val openAs: String? = null,
    val dietLabels: List<String>? = null,
    val healthLabels: List<String>? = null,
    val cautions: List<String>? = null,
    val ingredientLines: List<String>? = null,
    val calories: Double? = null,
    val totalTime: Double? = null,
    val yield: Double? = null,
    val cuisineType: List<String>? = null,
    val mealType: List<String>? = null,
    val dishType: List<String>? = null,
    val instructions: List<String>? = null,
)