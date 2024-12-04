package com.angelmascarell.collectorhub.data.repository

import android.net.http.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.angelmascarell.collectorhub.data.model.RegisterModel
import com.angelmascarell.collectorhub.data.network.SignUpService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import java.io.IOException

class SignUpRepository(
    private val signUpService: SignUpService,
    private val dataStore: DataStore<Preferences>
) {


    suspend fun doSignUp(registerModel: RegisterModel): Boolean {
        return try {
            val response = signUpService.register(registerModel)
            if (response.isSuccessful) {
                true
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = errorBody ?: "An unknown error occurred."
                Log.e("SignUpError", errorMessage)
                throw Exception(errorMessage) // Propagar el mensaje para manejarlo en la capa de UI.
            }
        } catch (e: Exception) {
            Log.e("SignUpException", e.message.toString())
            throw e
        }
    }


}