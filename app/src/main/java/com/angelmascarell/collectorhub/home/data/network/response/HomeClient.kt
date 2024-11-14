package com.angelmascarell.collectorhub.signin.data.network.response

import com.angelmascarell.collectorhub.signin.data.network.request.HomeRequest
import com.angelmascarell.collectorhub.signin.data.model.HomeModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface HomeClient {
    @POST("auth/login")
    suspend fun doSignIn(@Body requestBody: HomeRequest): Response<HomeModel>
}