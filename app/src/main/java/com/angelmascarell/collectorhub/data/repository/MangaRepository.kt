package com.angelmascarell.collectorhub.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import com.angelmascarell.collectorhub.data.model.MangaModel
import com.angelmascarell.collectorhub.data.model.RateCreateModel
import com.angelmascarell.collectorhub.data.model.RateModel
import com.angelmascarell.collectorhub.data.model.RateResponseList
import com.angelmascarell.collectorhub.data.network.MangaApiService
import kotlinx.coroutines.flow.first
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

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
                    false
                } else {
                    mangaList.any { it.id == mangaId }
                }
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun addReview(rateModel: RateCreateModel): Response<RateModel> {
        return try {
            apiService.addMangaReview(rateModel)
        } catch (e: Exception) {
            e.printStackTrace()
            Response.error(500, "Error al enviar la reseña".toResponseBody())
        }
    }

    suspend fun hasUserReviewed(mangaId: Long): Boolean {
        val response = apiService.getUserReview(mangaId)
        return response.isSuccessful && response.body() == true
    }


}