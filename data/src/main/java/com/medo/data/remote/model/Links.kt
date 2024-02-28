package com.medo.data.remote.model

import com.google.gson.annotations.SerializedName

data class Links(
    @SerializedName("self") val self: Self? = Self(),
    @SerializedName("next") val next: Next? = Next(),
)