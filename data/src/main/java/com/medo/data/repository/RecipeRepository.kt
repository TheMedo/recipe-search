package com.medo.data.repository

import android.util.Log
import com.medo.data.local.dao.SearchResultsDao
import com.medo.data.local.mapper.toRecipesWithIngredients
import com.medo.data.local.model.RecipeWithIngredients
import com.medo.data.remote.model.SearchRecipesResponse
import com.medo.data.remote.service.ApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface RecipeRepository {
    fun getCurrentSearchResults(): Flow<List<RecipeWithIngredients>>

    suspend fun searchRecipes(query: String): SearchRecipesResponse?

    suspend fun searchRecipesNextPage(url: String, from: Int): SearchRecipesResponse?

    fun getRecipe(uri: String): Flow<RecipeWithIngredients>
}

class EdamamRecipeRepository @Inject constructor(
    private val remote: ApiService,
    private val local: SearchResultsDao,
) : RecipeRepository {

    override fun getCurrentSearchResults(): Flow<List<RecipeWithIngredients>> = local.getRecipesWithIngredients()

    override suspend fun searchRecipes(query: String): SearchRecipesResponse? {
        try {
            val response = remote.searchRecipes(query)
            if (!response.isSuccessful) return null

            val data = response.body() ?: return null

            local.deleteRecipesWithIngredients()
            local.insertRecipesWithIngredients(data.hits.toRecipesWithIngredients())

            return data
        } catch (e: Exception) {
            Log.e(javaClass.name, "searchRecipes: ", e)
            return null
        }
    }

    override suspend fun searchRecipesNextPage(url: String, from: Int): SearchRecipesResponse? {
        try {
            val response = remote.searchRecipesByUrl(url)
            if (!response.isSuccessful) return null

            val data = response.body() ?: return null

            local.insertRecipesWithIngredients(data.hits.toRecipesWithIngredients(offset = from))

            return data
        } catch (e: Exception) {
            Log.e(javaClass.name, "searchRecipes: ", e)
            return null
        }
    }

    override fun getRecipe(uri: String): Flow<RecipeWithIngredients> = local.getRecipe(uri)
}