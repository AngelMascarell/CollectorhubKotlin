package com.angelmascarell.collectorhub.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.angelmascarell.collectorhub.data.network.RateService
import com.angelmascarell.collectorhub.data.network.TaskService
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class TaskRepository(private val apiService: TaskService,
                     private val dataStore: DataStore<Preferences>
) {

    suspend fun checkTaskCompletion(): Response<String> {
        return try {
            val response = apiService.checkTasks()
            if (response.isSuccessful) {
                val responseBody = response.body()?.string() ?: "Respuesta vacía"
                Response.success(responseBody)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Error desconocido"
                Response.error(response.code(), errorMessage.toResponseBody())
            }
        } catch (e: Exception) {
            Response.error(500, "Excepción: ${e.localizedMessage}".toResponseBody())
        }
    }
}