package com.medo.data.model

import com.google.gson.annotations.SerializedName

data class AdditionalProp3(
    @SerializedName("label") val label: String? = null,
    @SerializedName("quantity") val quantity: Double? = null,
    @SerializedName("unit") val unit: String? = null,
)