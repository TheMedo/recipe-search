package com.medo.data.remote.service

import com.medo.data.remote.model.SearchRecipesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {

    @GET("api/recipes/v2")
    suspend fun searchRecipes(
        @Query("q") query: String,
    ): Response<SearchRecipesResponse>

    @GET
    suspend fun searchRecipesByUrl(
        @Url url: String,
    ): Response<SearchRecipesResponse>
}