package com.medo.data.local.model

import androidx.room.Embedded
import androidx.room.Relation

data class RecipeWithIngredients(
    @Embedded val recipe: Recipe,
    @Relation(
        parentColumn = "uri",
        entityColumn = "recipeUri"
    )
    val ingredients: List<Ingredient>,
)