package com.medo.data.model

import com.google.gson.annotations.SerializedName

data class SearchRecipesResponse(
    @SerializedName("from") val from: Int? = null,
    @SerializedName("to") val to: Int? = null,
    @SerializedName("count") val count: Int? = null,
    @SerializedName("_links") val links: Links? = Links(),
    @SerializedName("hits") val hits: ArrayList<Hits> = arrayListOf(),
)