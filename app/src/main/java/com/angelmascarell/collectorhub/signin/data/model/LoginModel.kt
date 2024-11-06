package com.angelmascarell.collectorhub.signin.data.model

import com.google.gson.annotations.SerializedName

data class LoginModel(
    @SerializedName("accessToken") val accessToken: String,
)
