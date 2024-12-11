package com.angelmascarell.collectorhub.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import com.angelmascarell.collectorhub.data.model.MangaResponseList
import com.angelmascarell.collectorhub.data.model.UpdateUserResponse
import com.angelmascarell.collectorhub.data.model.UserModel
import com.angelmascarell.collectorhub.data.network.UserService
import kotlinx.coroutines.flow.first
import retrofit2.Response

class UserRepository(
    private val apiService: UserService,
    private val dataStore: DataStore<Preferences>
) {
    private suspend fun getUserId(): Long {
        val userIdKey = longPreferencesKey("userId")
        return dataStore.data.first()[userIdKey] ?: 0L
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

    suspend fun checkIfMangaInDesiredCollection(mangaId: Long): Boolean {
        return try {
            val response = apiService.getUserDesiredMangas()

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


    suspend fun getUserMangas(): MangaResponseList {
        return try {
            val response = apiService.getUserMangas()
            if (response.isSuccessful) {
                response.body() ?: throw Exception("Respuesta vacía de la API")
            } else {
                throw Exception("Error en la llamada a la API: ${response.code()}")
            }
        } catch (e: Exception) {
            throw Exception("Error al obtener los mangas del usuario", e)
        }
    }

    suspend fun getUserProfile(): UserModel {
        return apiService.searchUserById()
    }

    suspend fun addDesiredMangaToUser(mangaId: Long): Response<String> {
        return apiService.addDesiredMangaToUser(mangaId)
    }

    suspend fun getUserDesiredMangas(): MangaResponseList {
        return try {
            val response = apiService.getUserDesiredMangas()
            if (response.isSuccessful) {
                response.body() ?: throw Exception("Respuesta vacía de la API")
            } else {
                throw Exception("Error en la llamada a la API: ${response.code()}")
            }
        } catch (e: Exception) {
            throw Exception("Error al obtener los mangas deseados del usuario", e)
        }
    }

    suspend fun getAuthenticatedUser(): String? {
        val user = apiService.getAuthenticatedUser()
        return user.profileImageUrl

    }

    suspend fun updateAuthenticatedUser(userModel: UserModel): UpdateUserResponse {
        return apiService.updateUser(userModel)
    }
}