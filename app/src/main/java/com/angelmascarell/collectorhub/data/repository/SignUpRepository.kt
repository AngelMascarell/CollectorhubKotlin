package com.angelmascarell.collectorhub.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.angelmascarell.collectorhub.data.model.RegisterModel
import com.angelmascarell.collectorhub.data.network.AuthService

class SignUpRepository(
    private val authService: AuthService,
    private val dataStore: DataStore<Preferences>
) {


    suspend fun doSignUp(registerModel: RegisterModel): Boolean {
        return try {
            val response = authService.register(registerModel)
            if (response.isSuccessful) {
                true
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = errorBody ?: "An unknown error occurred."
                Log.e("SignUpError", errorMessage)
                throw Exception(errorMessage)
            }
        } catch (e: Exception) {
            Log.e("SignUpException", e.message.toString())
            throw e
        }
    }


}