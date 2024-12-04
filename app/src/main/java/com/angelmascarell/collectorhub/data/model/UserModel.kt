package com.angelmascarell.collectorhub.data.model

import java.time.LocalDate

data class UserModel(
    val username: String,
    val email: String,
    val birthdate: LocalDate?,
    val registerDate: LocalDate?,
    val isPremium: Boolean = false,
    val premiumStartDate: LocalDate? = null,
    val premiumEndDate: LocalDate? = null,
    val profileImageUrl: String? = null
)