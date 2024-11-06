package com.angelmascarell.collectorhub.signin.data.network.response

import com.angelmascarell.collectorhub.signin.data.network.request.LoginRequest
import com.angelmascarell.collectorhub.signin.data.model.LoginModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginClient {
    @POST("auth/login")
    suspend fun doSignIn(@Body requestBody: LoginRequest): Response<LoginModel>
}