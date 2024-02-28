package com.medo.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey
    val uri: String,
    val index: Int,
    val title: String?,
    val image: String?,
    val shareAs: String?,
    val openAs: String?,
    val dietLabels: List<String>?,
    val healthLabels: List<String>?,
    val cautions: List<String>?,
    val ingredientLines: List<String>?,
    val calories: Double?,
    val totalTime: Double?,
    val yield: Double?,
    val cuisineType: List<String>?,
    val mealType: List<String>?,
    val dishType: List<String>?,
    val instructions: List<String>?,
)