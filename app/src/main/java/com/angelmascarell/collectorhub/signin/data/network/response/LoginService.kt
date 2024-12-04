package com.angelmascarell.collectorhub.signin.data.network.response

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.angelmascarell.collectorhub.home.data.network.request.HomeRequest
import com.angelmascarell.collectorhub.core.routes.Routes
import com.angelmascarell.collectorhub.home.data.network.response.HomeClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginService @Inject constructor(
    private val api: HomeClient,
    private val dataStore: DataStore<Preferences>
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
                        saveTokensToDataStore(loginResponse.accessToken)
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
            Routes.SignInScreenRoute.route // Ruta de inicio de sesión predeterminada en caso de error
        }
    }

    private suspend fun saveTokensToDataStore(accessToken: String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey("accessToken")] = accessToken
        }
        // Verificar si el token fue guardado correctamente
        val savedToken = dataStore.data.first()[stringPreferencesKey("accessToken")]
        Log.d("TokenManager", "Token guardado: $savedToken")
    }

}
