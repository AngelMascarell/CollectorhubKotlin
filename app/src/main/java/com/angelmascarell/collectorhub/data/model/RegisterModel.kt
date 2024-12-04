package com.angelmascarell.collectorhub.data.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class RegisterModel(
    @SerializedName("username") val username: String,
    @SerializedName("password")  val password: String,
    @SerializedName("email") val email: String,
    @SerializedName("birthdate") val birthdate: LocalDate,
)