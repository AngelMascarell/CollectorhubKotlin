
package com.angelmascarell.collectorhub.home.data.network.request
import com.google.gson.annotations.SerializedName

data class HomeRequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String
)
