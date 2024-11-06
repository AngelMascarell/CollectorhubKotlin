
package com.angelmascarell.collectorhub.signin.data.network.request
import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String
)
