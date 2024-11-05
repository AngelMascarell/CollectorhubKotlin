package com.angelmascarell.collectorhubApp.signin.presentation

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angelmascarell.collectorhubApp.core.routes.Routes
import com.angelmascarell.collectorhubApp.signin.data.network.response.LoginService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.FormBody
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    var loginService: LoginService,
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _isLogin = MutableLiveData<Boolean>(false)
    val isLogin: LiveData<Boolean> = _isLogin

    private val _redirectRoute = MutableLiveData<String>() // Para manejar la ruta de redirección
    val redirectRoute: LiveData<String> = _redirectRoute

    fun login(callback: (Boolean) -> Unit) {
        _isLogin.value = false
        val usernameValue = username.value.orEmpty()
        val passwordValue = password.value.orEmpty()

        if (usernameValue.isBlank() || passwordValue.isBlank()) {
            callback.invoke(_isLogin.value!!)
            return
        }

        val requestBody = FormBody.Builder()
            .add("username", usernameValue)
            .add("password", passwordValue)
            .build()

        viewModelScope.launch {
            val result = loginService.doSignIn(requestBody)

            // Verifica si la ruta devuelta es para la pantalla de inicio
            if (result == Routes.HomeScreenRoute.route) {
                _isLogin.value = true // Inicio de sesión exitoso
            } else {
                _isLogin.value = false // Inicio de sesión fallido
            }

            _redirectRoute.value = result // Actualiza la ruta de redirección
            callback.invoke(_isLogin.value!!)
        }
    }


    fun onSignInChanged(username: String, password: String) {
        _username.value = username
        _password.value = password
    }

    private suspend fun saveEmailToDataStore(username: String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey("username")] = username
        }
    }
}