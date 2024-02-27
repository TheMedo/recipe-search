package com.medo.data.model

import com.google.gson.annotations.SerializedName

data class TotalDaily(
    @SerializedName("additionalProp1") val additionalProp1: AdditionalProp1? = AdditionalProp1(),
    @SerializedName("additionalProp2") val additionalProp2: AdditionalProp2? = AdditionalProp2(),
    @SerializedName("additionalProp3") val additionalProp3: AdditionalProp3? = AdditionalProp3(),
)