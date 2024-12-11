package com.angelmascarell.collectorhub.data.network

import com.angelmascarell.collectorhub.data.model.RegisterModel
import com.angelmascarell.collectorhub.data.model.TokenModel
import com.angelmascarell.collectorhub.data.request.HomeRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("auth/register")
    suspend fun register(@Body registerModel: RegisterModel): Response<TokenModel>

    @POST("auth/login")
    suspend fun doSignIn(@Body requestBody: HomeRequest): Response<TokenModel>

    @POST("auth/logout")
    suspend fun doLogOut(): Response<String>
}