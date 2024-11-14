package com.angelmascarell.collectorhub.data.repository

import com.angelmascarell.collectorhub.data.model.MangaModel
import com.angelmascarell.collectorhub.data.network.MangaApiService

class MangaRepository(private val apiService: MangaApiService) {

    suspend fun getMangas(): List<MangaModel> {
        val response = apiService.getMangas()
        return response.mangaResponseList
    }
}