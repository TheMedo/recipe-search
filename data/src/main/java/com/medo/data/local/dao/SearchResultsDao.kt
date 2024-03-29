package com.medo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.medo.data.local.model.Ingredient
import com.medo.data.local.model.Recipe
import com.medo.data.local.model.RecipeWithIngredients
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchResultsDao {
    @Transaction
    @Query("SELECT * FROM recipes ORDER BY `index`")
    fun getRecipesWithIngredients(): Flow<List<RecipeWithIngredients>>

    @Transaction
    @Query("SELECT * FROM recipes WHERE uri = :uri")
    fun getRecipe(uri: String): Flow<RecipeWithIngredients>

    @Insert
    suspend fun insertRecipe(recipe: Recipe)

    @Insert
    suspend fun insertIngredient(ingredients: List<Ingredient>)

    @Transaction
    suspend fun insertRecipesWithIngredients(recipesWithIngredient: List<RecipeWithIngredients>) =
        recipesWithIngredient.forEach {
            insertRecipe(it.recipe)
            insertIngredient(it.ingredients)
        }

    @Query("DELETE FROM recipes")
    suspend fun deleteRecipes()

    @Query("DELETE FROM ingredients")
    suspend fun deleteIngredients()

    @Transaction
    suspend fun deleteRecipesWithIngredients() {
        deleteRecipes()
        deleteIngredients()
    }
}