package com.angelmascarell.collectorhub.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angelmascarell.collectorhub.data.model.RegisterModel
import com.angelmascarell.collectorhub.data.repository.SignUpRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpRepository: SignUpRepository
) : ViewModel() {

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _birthdate = MutableLiveData<LocalDate?>()
    val birthdate: LiveData<LocalDate?> = _birthdate

    private val _isLogin = MutableLiveData<Boolean>()
    val isLogin: LiveData<Boolean> = _isLogin


    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun signUp(callback: (Boolean, String?) -> Unit) {
        _isLogin.value = false
        val userName = username.value.orEmpty()
        val email = email.value.orEmpty()
        val password = password.value.orEmpty()
        val birthdate = birthdate.value

        Log.e("userName", userName)
        Log.e("email", email)
        Log.e("password", password)
        Log.e("birthdate", birthdate.toString())

        if (userName.isBlank() || email.isBlank() || password.isBlank() || birthdate == null) {
            callback.invoke(false, "All fields must be filled.")
            return
        }

        val requestBody = RegisterModel(
            username = userName,
            email = email,
            password = password,
            birthdate = birthdate
        )

        viewModelScope.launch {
            try {
                _isLogin.value = signUpRepository.doSignUp(requestBody)
                callback.invoke(_isLogin.value!!, null)
            } catch (e: Exception) {
                callback.invoke(false, e.message)
            }
        }
    }


    fun onSignUpChanged(username: String, email: String, password: String, birthdate: LocalDate?) {
        _username.value = username
        _email.value = email
        _password.value = password
        _birthdate.value = birthdate
    }
}