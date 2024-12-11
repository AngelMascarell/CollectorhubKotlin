package com.angelmascarell.collectorhub.data.network

import com.angelmascarell.collectorhub.data.model.MangaModel
import com.angelmascarell.collectorhub.data.model.MangaResponseList
import com.angelmascarell.collectorhub.data.model.ObtainMangaResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MangaApiService {

    @GET("manga/getAll")
    suspend fun getMangas(): ObtainMangaResponse

    @GET("manga/personalized/{userId}")
    suspend fun getPersonalizedMangas(@Path("userId") userId: Long): ObtainMangaResponse

    @GET("manga/getCompleted")
    suspend fun getCompletedMangas(): ObtainMangaResponse

    @GET("manga/{id}")
    suspend fun getOneManga(@Path("id") id: Long): MangaModel

    @GET("manga/recent-30-days")
    suspend fun getNewMangas(): Response<MangaResponseList>

    @GET("manga/search")
    suspend fun searchMangaByTitle(@Query("name") name: String): MangaModel

}