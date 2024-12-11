package com.angelmascarell.collectorhub.data.network

import com.angelmascarell.collectorhub.data.model.RateCreateModel
import com.angelmascarell.collectorhub.data.model.RateModel
import com.angelmascarell.collectorhub.data.model.RateResponseList
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RateService {

    @GET("rate/manga/{mangaId}/average")
    suspend fun getAverageRateByMangaId(@Path("mangaId") mangaId: Long): Int

    @GET("rate/manga/{mangaId}")
    suspend fun getRatesByMangaId(@Path("mangaId") mangaId: Long): RateResponseList

    @POST("rate/new")
    suspend fun addMangaReview(@Body rateModel: RateCreateModel): Response<RateModel>

    @GET("rate/user-review/{mangaId}")
    suspend fun getUserReview(@Path("mangaId") mangaId: Long): Response<Boolean>

}