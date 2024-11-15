package com.angelmascarell.collectorhub.data.network

import com.angelmascarell.collectorhub.data.model.MangaModel
import com.angelmascarell.collectorhub.data.model.ObtainMangaResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface MangaApiService {

    @GET("manga/getAll")
    suspend fun getMangas(): ObtainMangaResponse

    @GET("manga/personalized/{userId}")
    suspend fun getPersonalizedMangas(@Path("userId") userId: Long): ObtainMangaResponse

    @GET("manga/getCompleted")
    suspend fun getCompletedMangas(): ObtainMangaResponse
}