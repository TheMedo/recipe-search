package com.medo.data.model

import com.google.gson.annotations.SerializedName

data class Self(
    @SerializedName("href") val href: String? = null,
    @SerializedName("title") val title: String? = null,
)