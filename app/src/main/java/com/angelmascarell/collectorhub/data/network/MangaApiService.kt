package com.angelmascarell.collectorhub.data.network

import com.angelmascarell.collectorhub.data.model.MangaModel
import com.angelmascarell.collectorhub.data.model.ObtainMangaResponse
import retrofit2.http.GET

interface MangaApiService {

    @GET("manga/getAll")
    suspend fun getMangas(): ObtainMangaResponse
}