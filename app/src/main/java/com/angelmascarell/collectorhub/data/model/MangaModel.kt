package com.angelmascarell.collectorhub.data.model

data class MangaModel(
    val id: Long,
    val title: String,
    val author: String,
    val genre: String,
    val chapters: Int,
    val completed: Boolean,
    val imageUrl: String
)
