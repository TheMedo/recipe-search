package com.medo.data.model

import com.google.gson.annotations.SerializedName

data class Hits(
    @SerializedName("recipe") val recipe: Recipe? = Recipe(),
    @SerializedName("_links") val links: Links? = Links(),
)