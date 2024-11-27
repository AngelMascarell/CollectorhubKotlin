package com.angelmascarell.collectorhub.data.model

import java.time.LocalDate

data class RateModel(
    val id: Long? = null,
    val userId: Long,
    val mangaId: Long,
    val rate: Int,
    val comment: String,
    val date: LocalDate
)
