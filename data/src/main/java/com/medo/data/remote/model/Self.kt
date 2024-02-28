package com.medo.data.remote.model

import com.google.gson.annotations.SerializedName

data class Self(
    @SerializedName("href") val href: String? = null,
    @SerializedName("title") val title: String? = null,
)