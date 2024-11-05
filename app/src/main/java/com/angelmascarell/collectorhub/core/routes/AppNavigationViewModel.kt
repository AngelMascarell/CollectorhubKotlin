package com.angelmascarell.collectorhubApp.core.routes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angelmascarell.collectorhubApp.signin.data.network.response.LoginService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.FormBody
import javax.inject.Inject

@HiltViewModel
class AppNavigationViewModel @Inject constructor(
    private val api: LoginService
) : ViewModel() {

    private val _firstScreen = MutableLiveData<String>(Routes.SignInScreenRoute.route) // Valor predeterminado
    val firstScreen: LiveData<String> = _firstScreen

    fun getFirstScreen(username: String, password: String) {
        Log.e("before-login", _firstScreen.value.toString())
        viewModelScope.launch {
            try {
                // Construir el FormBody con las credenciales
                val requestBody = FormBody.Builder()
                    .add("username", username)
                    .add("password", password)
                    .build()

                // Intenta obtener la pantalla inicial desde la API
                val result = api.doSignIn(requestBody)
                _firstScreen.value = result
                Log.e("api-login", "Login success: $result")
            } catch (e: Exception) {
                // Si hay una excepción, mantenemos SignInScreen como la pantalla predeterminada
                _firstScreen.value = Routes.SignInScreenRoute.route
                Log.e("getFirstScreen", "Exception: $e")
            }
            Log.e("after-login", _firstScreen.value.toString())
        }
    }
}

