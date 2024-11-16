package com.angelmascarell.collectorhub.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import com.angelmascarell.collectorhub.data.model.MangaModel
import com.angelmascarell.collectorhub.data.network.MangaApiService
import kotlinx.coroutines.flow.first

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
}