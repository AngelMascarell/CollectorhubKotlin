package com.angelmascarell.collectorhub.home.data.network.response

import com.angelmascarell.collectorhub.home.data.network.request.HomeRequest
import com.angelmascarell.collectorhub.signin.data.model.TokenModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface HomeClient {

    @POST("auth/login")
    suspend fun doSignIn(@Body requestBody: HomeRequest): Response<TokenModel>

    @POST("auth/logout")
    suspend fun doLogOut(): Response<String>
}