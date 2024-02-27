package com.medo.data.model

import com.google.gson.annotations.SerializedName

data class Thumbnail(
    @SerializedName("url") val url: String? = null,
    @SerializedName("width") val width: Int? = null,
    @SerializedName("height") val height: Int? = null,
)