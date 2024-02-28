package com.medo.data.repository

import android.util.Log
import com.medo.data.local.dao.SearchResultsDao
import com.medo.data.local.mapper.toRecipesWithIngredients
import com.medo.data.local.model.Favorite
import com.medo.data.local.model.RecipeWithIngredients
import com.medo.data.remote.model.SearchRecipesResponse
import com.medo.data.remote.service.ApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface RecipeRepository {
    suspend fun getCurrentSearchResults(): Flow<List<RecipeWithIngredients>>

    suspend fun searchRecipes(query: String): SearchRecipesResponse?

    suspend fun getFavorites(): Flow<List<Favorite>>

    suspend fun updateFavorite(favorite: Favorite)
}

class EdamamRecipeRepository @Inject constructor(
    private val remote: ApiService,
    private val local: SearchResultsDao,
) : RecipeRepository {

    override suspend fun getCurrentSearchResults(): Flow<List<RecipeWithIngredients>> =
        local.getRecipesWithIngredients()

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

    override suspend fun getFavorites(): Flow<List<Favorite>> = local.getFavorites()

    override suspend fun updateFavorite(favorite: Favorite) = local.updateFavorite(favorite)
}