package com.angelmascarell.collectorhub.signin.presentation

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angelmascarell.collectorhub.home.data.network.request.HomeRequest
import com.angelmascarell.collectorhub.core.routes.Routes
import com.angelmascarell.collectorhub.home.data.network.response.HomeService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    var loginService: HomeService,
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

        // Verifica si el nombre de usuario y la contraseña no están vacíos
        if (usernameValue.isBlank() || passwordValue.isBlank()) {
            callback.invoke(_isLogin.value!!)
            return
        }

        // Crear el LoginRequest con los valores de usuario y contraseña
        val loginRequest = HomeRequest(usernameValue, passwordValue)

        viewModelScope.launch {
            try {
                // Usar el loginRequest en lugar del FormBody
                val result = loginService.doSignIn(loginRequest.username, loginRequest.password)

                // Verifica si la ruta devuelta es para la pantalla de inicio
                if (result == Routes.HomeScreenRoute.route) {
                    _isLogin.value = true // Inicio de sesión exitoso
                } else {
                    _isLogin.value = false // Inicio de sesión fallido
                }

                // Actualiza la ruta de redirección
                _redirectRoute.value = result
                callback.invoke(_isLogin.value!!)
            } catch (e: Exception) {
                // Maneja excepciones si las hubiera
                _isLogin.value = false
                _redirectRoute.value = Routes.SignInScreenRoute.route // Ruta de inicio de sesión en caso de error
                callback.invoke(_isLogin.value!!)
            }
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