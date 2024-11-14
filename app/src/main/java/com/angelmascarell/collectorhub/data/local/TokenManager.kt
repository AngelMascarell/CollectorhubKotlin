package com.angelmascarell.collectorhub.data.local

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

// Usa el delegado DataStore desde el contexto de la aplicación
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token_data_store")

class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    suspend fun getToken(): String {
        val token = context.dataStore.data
            .map { preferences ->
                preferences[stringPreferencesKey("accessToken")] ?: ""
            }
            .first()

        Log.d("TokenManager", "Retrieved token: $token")
        return token
    }


    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey("accessToken")] = token
            Log.d("TokenManager", "Saving token: $token")

        }
    }

}
