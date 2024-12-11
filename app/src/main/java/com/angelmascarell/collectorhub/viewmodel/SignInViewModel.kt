package com.angelmascarell.collectorhub.viewmodel

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angelmascarell.collectorhub.data.request.HomeRequest
import com.angelmascarell.collectorhub.core.routes.Routes
import com.angelmascarell.collectorhub.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private var loginService: AuthRepository,
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _isLogin = MutableLiveData<Boolean>(false)
    val isLogin: LiveData<Boolean> = _isLogin

    private val _redirectRoute = MutableLiveData<String>()
    val redirectRoute: LiveData<String> = _redirectRoute

    fun login(callback: (Boolean) -> Unit) {
        _isLogin.value = false
        val usernameValue = username.value.orEmpty()
        val passwordValue = password.value.orEmpty()

        if (usernameValue.isBlank() || passwordValue.isBlank()) {
            callback.invoke(_isLogin.value!!)
            return
        }

        val loginRequest = HomeRequest(usernameValue, passwordValue)

        viewModelScope.launch {
            try {
                val result = loginService.doSignIn(loginRequest.username, loginRequest.password)

                if (result == Routes.HomeScreenRoute.route) {
                    _isLogin.value = true
                } else {
                    _isLogin.value = false
                }

                _redirectRoute.value = result
                callback.invoke(_isLogin.value!!)
            } catch (e: Exception) {
                _isLogin.value = false
                _redirectRoute.value = Routes.SignInScreenRoute.route
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