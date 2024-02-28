package com.medo.data.remote.model

import com.google.gson.annotations.SerializedName

data class Ingredients(
    @SerializedName("text") val text: String? = null,
    @SerializedName("quantity") val quantity: Double? = null,
    @SerializedName("measure") val measure: String? = null,
    @SerializedName("food") val food: String? = null,
    @SerializedName("weight") val weight: Double? = null,
    @SerializedName("foodId") val foodId: String? = null,
    @SerializedName("foodCategory") val foodCategory: String? = null,
    @SerializedName("image") val image: String? = null,
)