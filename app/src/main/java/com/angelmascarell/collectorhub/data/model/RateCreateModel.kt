package com.angelmascarell.collectorhub.data.model

import java.time.LocalDate

data class RateCreateModel(
    val mangaId: Long,
    val rate: Int,
    val comment: String,
)