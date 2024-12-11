package com.angelmascarell.collectorhub.core.routes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angelmascarell.collectorhub.data.request.HomeRequest
import com.angelmascarell.collectorhub.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppNavigationViewModel @Inject constructor(
    private val api: AuthRepository
) : ViewModel() {

    private val _firstScreen =
        MutableLiveData<String>(Routes.SignInScreenRoute.route)
    val firstScreen: LiveData<String> = _firstScreen

    fun getFirstScreen(username: String, password: String) {
        Log.e("before-login", _firstScreen.value.toString())
        viewModelScope.launch {
            try {
                val loginRequest = HomeRequest(username, password)

                val result = api.doSignIn(loginRequest.username, loginRequest.password)
                _firstScreen.value = result
                Log.e("api-login", "Login success: $result")
            } catch (e: Exception) {
                _firstScreen.value = Routes.SignInScreenRoute.route
                Log.e("getFirstScreen", "Exception: $e")
            }
            Log.e("after-login", _firstScreen.value.toString())
        }
    }
}
