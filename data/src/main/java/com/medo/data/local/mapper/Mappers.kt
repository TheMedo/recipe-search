package com.medo.data.local.mapper

import com.medo.data.local.model.Ingredient
import com.medo.data.local.model.Recipe
import com.medo.data.local.model.RecipeWithIngredients
import com.medo.data.remote.model.Hits
import com.medo.data.remote.model.Ingredients

fun ArrayList<Hits>.toRecipesWithIngredients(offset: Int = 0): List<RecipeWithIngredients> = mapIndexed { index, hit ->
    RecipeWithIngredients(
        recipe = hit.toRecipe(offset + index),
        ingredients = hit.recipe?.ingredients?.toIngredients(hit.recipe.uri ?: "") ?: emptyList(),
    )
}

private fun Hits.toRecipe(index: Int): Recipe = Recipe(
    uri = recipe?.uri ?: "",
    index = index,
    title = recipe?.label,
    image = recipe?.image,
    shareAs = recipe?.shareAs,
    openAs = links?.self?.href,
    dietLabels = recipe?.dietLabels,
    healthLabels = recipe?.healthLabels,
    cautions = recipe?.cautions,
    ingredientLines = recipe?.ingredientLines,
    calories = recipe?.calories,
    totalTime = recipe?.totalTime,
    yield = recipe?.yield,
    cuisineType = recipe?.cuisineType,
    mealType = recipe?.mealType,
    dishType = recipe?.dishType,
    instructions = recipe?.instructions,
)

private fun ArrayList<Ingredients>.toIngredients(recipeUri: String) = map {
    Ingredient(
        recipeUri = recipeUri,
        text = it.text,
        quantity = it.quantity,
        measure = it.measure,
        food = it.food,
        weight = it.weight,
        foodCategory = it.foodCategory,
        foodId = it.foodId,
        image = it.image,
    )
}