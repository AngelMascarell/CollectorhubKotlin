package com.angelmascarell.collectorhub.data.network

import com.angelmascarell.collectorhub.data.model.RegisterModel
import com.angelmascarell.collectorhub.signin.data.model.TokenModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SignUpService {

    @POST("auth/register")
    suspend fun register(@Body registerModel: RegisterModel): Response<TokenModel>
}