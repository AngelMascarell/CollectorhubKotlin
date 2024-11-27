package com.angelmascarell.collectorhub.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import com.angelmascarell.collectorhub.data.model.MangaModel
import com.angelmascarell.collectorhub.data.model.RateModel
import com.angelmascarell.collectorhub.data.model.RateResponseList
import com.angelmascarell.collectorhub.data.network.MangaApiService
import kotlinx.coroutines.flow.first
import retrofit2.Response

class MangaRepository(private val apiService: MangaApiService, private val dataStore: DataStore<Preferences>) {

    suspend fun getMangas(): List<MangaModel> {
        val response = apiService.getMangas()
        return response.mangaResponseList
    }

    // Función para obtener el userId guardado en DataStore
    private suspend fun getUserId(): Long {
        val userIdKey = longPreferencesKey("userId")
        return dataStore.data.first()[userIdKey] ?: 0L
    }

    // Función para obtener mangas personalizados
    suspend fun getPersonalizedMangas(): List<MangaModel> {
        val userId = getUserId()
        if (userId != 0L) {
            val response = apiService.getPersonalizedMangas(userId)
            return response.mangaResponseList
        } else {
            throw Exception("User not logged in or invalid userId")
        }
    }

    suspend fun getMangaDetails(mangaId: Long): MangaModel {
        val response = apiService.getOneManga(mangaId)
        return response
    }

    suspend fun getAverageRateByMangaId(mangaId: Long): Int {
        return apiService.getAverageRateByMangaId(mangaId)
    }

    suspend fun getRatesByMangaId(mangaId: Long): RateResponseList {
        return apiService.getRatesByMangaId(mangaId)
    }

    suspend fun addMangaToUser(mangaId: Long): Response<String> {
        return apiService.addMangaToUser(mangaId)
    }

    suspend fun checkIfMangaInCollection(mangaId: Long): Boolean {
        return try {
            val response = apiService.getUserMangas()

            if (response.isSuccessful) {
                val mangaList = response.body()?.mangaResponseList
                if (mangaList.isNullOrEmpty()) {
                    // La lista está vacía, no está en la colección
                    false
                } else {
                    // Verificar si el mangaId está en la lista de respuestas
                    mangaList.any { it.id == mangaId }
                }
            } else {
                // Respuesta no exitosa, tratar como no está en la colección
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // En caso de error, tratar como no está en la colección
            false
        }
    }



    suspend fun addReview(mangaId: Long, review: String): Response<String> {
        // Lógica para guardar la reseña en el backend
        return apiService.addMangaReview(mangaId, review)
    }

}