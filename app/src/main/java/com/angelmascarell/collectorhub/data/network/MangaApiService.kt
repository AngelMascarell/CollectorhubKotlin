package com.angelmascarell.collectorhub.data.network

import com.angelmascarell.collectorhub.data.model.MangaModel
import com.angelmascarell.collectorhub.data.model.MangaResponseList
import com.angelmascarell.collectorhub.data.model.ObtainMangaResponse
import com.angelmascarell.collectorhub.data.model.RateCreateModel
import com.angelmascarell.collectorhub.data.model.RateModel
import com.angelmascarell.collectorhub.data.model.RateResponseList
import com.angelmascarell.collectorhub.data.model.UpdateUserResponse
import com.angelmascarell.collectorhub.data.model.UserModel
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @GET("rate/manga/{mangaId}/average")
    suspend fun getAverageRateByMangaId(@Path("mangaId") mangaId: Long): Int

    @GET("rate/manga/{mangaId}")
    suspend fun getRatesByMangaId(@Path("mangaId") mangaId: Long): RateResponseList

    @POST("user/add-manga/{mangaId}")
    suspend fun addMangaToUser(@Path("mangaId") mangaId: Long): Response<String>

    @GET("user/mangas")
    suspend fun getUserMangas(): Response<MangaResponseList>

    @GET("user/get-authenticated-user")
    suspend fun getAuthenticatedUser(): UserModel

    @POST("user/add-desired-manga/{mangaId}")
    suspend fun addDesiredMangaToUser(@Path("mangaId") mangaId: Long): Response<String>

    @GET("user/desired-mangas")
    suspend fun getUserDesiredMangas(): Response<MangaResponseList>

    @POST("rate/new")
    suspend fun addMangaReview(@Body rateModel: RateCreateModel): Response<RateModel>

    @GET("rate/user-review/{mangaId}")
    suspend fun getUserReview(@Path("mangaId") mangaId: Long): Response<Boolean>

    @GET("manga/search")
    suspend fun searchMangaByTitle(@Query("name") name: String): MangaModel

    @GET("user/getUserProfile")
    suspend fun searchUserById(): UserModel

    @PUT("user/upd-auth-user")
    suspend fun updateUser(@Body userModel: UserModel): UpdateUserResponse

    @POST("/task/checkCompletion")
    suspend fun checkTasks() : Response<ResponseBody>


}