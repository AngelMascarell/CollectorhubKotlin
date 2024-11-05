package com.angelmascarell.collectorhubApp.signin.data.network.response

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.angelmascarell.collectorhubApp.core.routes.Routes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import javax.inject.Inject

class LoginService @Inject constructor(
    private val api: LoginClient,
    private val dataStore: DataStore<Preferences>
) {
    suspend fun doSignIn(requestBody: FormBody): String {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.doSignIn(requestBody)
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        saveTokensToDataStore(loginResponse.accessToken)
                        return@withContext Routes.HomeScreenRoute.route // O ruta deseada para usuarios autenticados
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
    }
}
