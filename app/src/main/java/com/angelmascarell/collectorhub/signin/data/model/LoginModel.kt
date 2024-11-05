package com.angelmascarell.collectorhubApp.signin.data.model

import com.google.gson.annotations.SerializedName

data class LoginModel(
    @SerializedName("accessToken") val accessToken: String,
)
