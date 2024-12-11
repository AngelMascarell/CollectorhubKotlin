package com.angelmascarell.collectorhub.data.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.POST

interface TaskService {

    @POST("/task/checkCompletion")
    suspend fun checkTasks() : Response<ResponseBody>
}