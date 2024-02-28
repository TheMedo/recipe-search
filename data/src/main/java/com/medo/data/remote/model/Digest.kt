package com.medo.data.remote.model

import com.google.gson.annotations.SerializedName

data class Digest(
    @SerializedName("label") val label: String? = null,
    @SerializedName("tag") val tag: String? = null,
    @SerializedName("schemaOrgTag") val schemaOrgTag: String? = null,
    @SerializedName("total") val total: Double? = null,
    @SerializedName("hasRDI") val hasRDI: Boolean? = null,
    @SerializedName("daily") val daily: Double? = null,
    @SerializedName("unit") val unit: String? = null,
    @SerializedName("sub") val sub: ArrayList<Digest> = arrayListOf(),
)