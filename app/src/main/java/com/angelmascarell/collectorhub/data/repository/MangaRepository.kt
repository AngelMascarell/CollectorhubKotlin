package com.angelmascarell.collectorhub.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import com.angelmascarell.collectorhub.data.model.MangaModel
import com.angelmascarell.collectorhub.data.model.MangaResponseList
import com.angelmascarell.collectorhub.data.network.MangaApiService
import kotlinx.coroutines.flow.first

class MangaRepository(
    private val apiService: MangaApiService,
    private val dataStore: DataStore<Preferences>
) {

    suspend fun getMangas(): List<MangaModel> {
        val response = apiService.getMangas()
        return response.mangaResponseList
    }

    private suspend fun getUserId(): Long {
        val userIdKey = longPreferencesKey("userId")
        return dataStore.data.first()[userIdKey] ?: 0L
    }

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

    suspend fun getMangaByTitle(title: String): MangaModel? {
        return try {
            apiService.searchMangaByTitle(title)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getNewMangas(): MangaResponseList {
        return try {
            val response = apiService.getNewMangas()
            if (response.isSuccessful) {
                response.body() ?: throw Exception("Respuesta vacía de la API")
            } else {
                throw Exception("Error en la llamada a la API: ${response.code()}")
            }
        } catch (e: Exception) {
            throw Exception("Error al obtener los mangas deseados del usuario", e)
        }
    }

}