package com.angelmascarell.collectorhub.data.model

import com.google.gson.annotations.SerializedName

data class LoginModel(
    @SerializedName("accessToken") val accessToken: String,
)
