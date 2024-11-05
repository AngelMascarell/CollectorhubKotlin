package com.angelmascarell.collectorhubApp.signin.data.network.response

import com.angelmascarell.collectorhubApp.signin.data.model.LoginModel
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginClient {
    @POST("auth/login")
    suspend fun doSignIn(@Body requestBody: RequestBody): Response<LoginModel>
}