package com.angelmascarell.collectorhub.data.model

import com.google.gson.annotations.SerializedName

data class TokenModel(
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("userId") val userId: Long

)
