package com.medo.data.repository

import android.util.Log
import com.medo.data.model.SearchRecipesResponse
import com.medo.data.service.ApiService
import javax.inject.Inject

interface RecipeRepository {
    suspend fun searchRecipes(query: String): SearchRecipesResponse?
}

class EdamamRecipeRepository @Inject constructor(
    private val remote: ApiService,
) : RecipeRepository {

    override suspend fun searchRecipes(query: String): SearchRecipesResponse? {
        try {
            val response = remote.searchRecipes(query)
            if (!response.isSuccessful) return null

            val data = response.body() ?: return null
            // TODO persist data

            return data
        } catch (e: Exception) {
            Log.e(javaClass.name, "searchRecipes: ", e)
            return null
        }
    }
}