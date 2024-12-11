package com.angelmascarell.collectorhub.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.angelmascarell.collectorhub.data.model.RateCreateModel
import com.angelmascarell.collectorhub.data.model.RateModel
import com.angelmascarell.collectorhub.data.model.RateResponseList
import com.angelmascarell.collectorhub.data.network.RateService
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class RateRepository(private val apiService: RateService,
                     private val dataStore: DataStore<Preferences>
) {

    suspend fun getAverageRateByMangaId(mangaId: Long): Int {
        return apiService.getAverageRateByMangaId(mangaId)
    }

    suspend fun getRatesByMangaId(mangaId: Long): RateResponseList {
        return apiService.getRatesByMangaId(mangaId)
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