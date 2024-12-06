package com.angelmascarell.collectorhub.data.model

data class UpdateUserResponse(
    val user: UserModel,
    val token: String
)
