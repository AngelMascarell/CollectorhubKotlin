package com.angelmascarell.collectorhub.home.data.network.response

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.angelmascarell.collectorhub.home.data.network.request.HomeRequest
import com.angelmascarell.collectorhub.core.routes.Routes
import com.angelmascarell.collectorhub.data.local.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeService @Inject constructor(
    private val api: HomeClient,
    private val dataStore: DataStore<Preferences>,
    private val tokenManager: TokenManager
) {
    // Modificar el método para recibir username y password como parámetros
    suspend fun doSignIn(username: String, password: String): String {
        return withContext(Dispatchers.IO) {
            try {
                // Crear el LoginRequest con los parámetros recibidos
                val loginRequest = HomeRequest(username, password)
                val response = api.doSignIn(loginRequest)

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        saveTokensToDataStore(
                            loginResponse.accessToken,
                            loginResponse.userId
                        )  // Guardamos también el userId
                        return@withContext Routes.HomeScreenRoute.route // Ruta deseada para usuarios autenticados
                    } else {
                        Log.e("doSignIn", "LoginModel is null")
                    }
                } else {
                    Log.e("doSignIn", "Failed to sign in: ${response.code()}")
                }
            } catch (e: TimeoutCancellationException) {
                e.printStackTrace()
            }
            Routes.SignInScreenRoute.route // Ruta de inicio de sesión predeterminada en caso de error
        }
    }

    suspend fun logout() {
        api.doLogOut()
        tokenManager.clearToken()
    }

    private suspend fun saveTokensToDataStore(accessToken: String, userId: Long) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey("accessToken")] = accessToken
            preferences[longPreferencesKey("userId")] = userId  // Guardamos el userId
        }
        // Verificar si el token y el userId fueron guardados correctamente
        val savedToken = dataStore.data.first()[stringPreferencesKey("accessToken")]
        val savedUserId = dataStore.data.first()[longPreferencesKey("userId")]
        Log.d("TokenManager", "Token guardado: $savedToken, userId guardado: $savedUserId")
    }
}
