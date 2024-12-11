package com.angelmascarell.collectorhub.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.angelmascarell.collectorhub.data.request.HomeRequest
import com.angelmascarell.collectorhub.core.routes.Routes
import com.angelmascarell.collectorhub.data.local.TokenManager
import com.angelmascarell.collectorhub.data.network.AuthService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: AuthService,
    private val dataStore: DataStore<Preferences>,
    private val tokenManager: TokenManager
) {
    suspend fun doSignIn(username: String, password: String): String {
        return withContext(Dispatchers.IO) {
            try {
                val loginRequest = HomeRequest(username, password)
                val response = api.doSignIn(loginRequest)

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        saveTokensToDataStore(
                            loginResponse.accessToken,
                            loginResponse.userId
                        )
                        return@withContext Routes.HomeScreenRoute.route
                    } else {
                        Log.e("doSignIn", "LoginModel is null")
                    }
                } else {
                    Log.e("doSignIn", "Failed to sign in: ${response.code()}")
                }
            } catch (e: TimeoutCancellationException) {
                e.printStackTrace()
            }
            Routes.SignInScreenRoute.route
        }
    }

    suspend fun logout() {
        api.doLogOut()
        tokenManager.clearToken()
    }

    private suspend fun saveTokensToDataStore(accessToken: String, userId: Long) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey("accessToken")] = accessToken
            preferences[longPreferencesKey("userId")] = userId
        }
        val savedToken = dataStore.data.first()[stringPreferencesKey("accessToken")]
        val savedUserId = dataStore.data.first()[longPreferencesKey("userId")]
        Log.d("TokenManager", "Token guardado: $savedToken, userId guardado: $savedUserId")
    }
}
