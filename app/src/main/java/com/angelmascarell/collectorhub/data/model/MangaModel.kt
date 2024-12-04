package com.angelmascarell.collectorhub.data.model

import java.time.LocalDate

data class MangaModel(
    val id: Long,
    val title: String,
    val author: String,
    val genreId: Long,
    val chapters: Int,
    val completed: Boolean,
    val imageUrl: String,
    val synopsis: String,
    val releaseDate: LocalDate,
)
