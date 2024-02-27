package com.medo.data.model

import com.google.gson.annotations.SerializedName

data class Next(
    @SerializedName("href") val href: String? = null,
    @SerializedName("title") val title: String? = null,
)