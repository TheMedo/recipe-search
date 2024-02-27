package com.medo.data.model

import com.google.gson.annotations.SerializedName

data class Images(
    @SerializedName("THUMBNAIL") val thumbnail: Thumbnail? = Thumbnail(),
    @SerializedName("SMALL") val small: Small? = Small(),
    @SerializedName("REGULAR") val regular: Regular? = Regular(),
    @SerializedName("LARGE") val large: Large? = Large(),
)