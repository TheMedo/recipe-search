package com.medo.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ingredients")
data class Ingredient(
    @PrimaryKey(autoGenerate = true)
    val ingredientId: Int = 0,
    val recipeUri: String,
    val text: String?,
    val quantity: Double?,
    val measure: String?,
    val food: String?,
    val weight: Double?,
    val foodCategory: String?,
    val foodId: String?,
    val image: String?,
)