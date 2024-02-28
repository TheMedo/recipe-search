package com.medo.data.remote.model

import com.google.gson.annotations.SerializedName

data class Recipe(
    @SerializedName("uri") val uri: String? = null,
    @SerializedName("label") val label: String? = null,
    @SerializedName("image") val image: String? = null,
    @SerializedName("images") val images: Images? = Images(),
    @SerializedName("source") val source: String? = null,
    @SerializedName("url") val url: String? = null,
    @SerializedName("shareAs") val shareAs: String? = null,
    @SerializedName("yield") val yield: Double? = null,
    @SerializedName("dietLabels") val dietLabels: ArrayList<String> = arrayListOf(),
    @SerializedName("healthLabels") val healthLabels: ArrayList<String> = arrayListOf(),
    @SerializedName("cautions") val cautions: ArrayList<String> = arrayListOf(),
    @SerializedName("ingredientLines") val ingredientLines: ArrayList<String> = arrayListOf(),
    @SerializedName("ingredients") val ingredients: ArrayList<Ingredients> = arrayListOf(),
    @SerializedName("calories") val calories: Double? = null,
    @SerializedName("glycemicIndex") val glycemicIndex: Double? = null,
    @SerializedName("inflammatoryIndex") val inflammatoryIndex: Double? = null,
    @SerializedName("totalCO2Emissions") val totalCO2Emissions: Double? = null,
    @SerializedName("co2EmissionsClass") val co2EmissionsClass: String? = null,
    @SerializedName("totalTime") val totalTime: Double? = null,
    @SerializedName("totalWeight") val totalWeight: Double? = null,
    @SerializedName("cuisineType") val cuisineType: ArrayList<String> = arrayListOf(),
    @SerializedName("mealType") val mealType: ArrayList<String> = arrayListOf(),
    @SerializedName("dishType") val dishType: ArrayList<String> = arrayListOf(),
    @SerializedName("instructions") val instructions: ArrayList<String> = arrayListOf(),
    @SerializedName("tags") val tags: ArrayList<String> = arrayListOf(),
    @SerializedName("externalId") val externalId: String? = null,
    @SerializedName("digest") val digest: ArrayList<Digest> = arrayListOf(),
)