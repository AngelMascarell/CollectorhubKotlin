package com.angelmascarell.collectorhub.data.network

import com.angelmascarell.collectorhub.data.model.MangaResponseList
import com.angelmascarell.collectorhub.data.request.HomeRequest
import com.angelmascarell.collectorhub.data.model.TokenModel
import com.angelmascarell.collectorhub.data.model.UpdateUserResponse
import com.angelmascarell.collectorhub.data.model.UserModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserService {

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

    @GET("user/getUserProfile")
    suspend fun searchUserById(): UserModel

    @PUT("user/upd-auth-user")
    suspend fun updateUser(@Body userModel: UserModel): UpdateUserResponse
}