package com.angelmascarell.collectorhub.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angelmascarell.collectorhub.data.local.TokenManager
import com.angelmascarell.collectorhub.data.model.UserModel
import com.angelmascarell.collectorhub.data.repository.MangaRepository
import com.angelmascarell.collectorhub.data.repository.AuthRepository
import com.angelmascarell.collectorhub.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val mangaApi: MangaRepository,
    private val tokenManager: TokenManager,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _userProfile = MutableStateFlow<UserModel?>(null)
    val userProfile: StateFlow<UserModel?> = _userProfile

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _updateResult = MutableStateFlow<String>("")
    val updateResult: StateFlow<String> get() = _updateResult

    fun updateUserProfile(updatedProfile: UserModel) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = userRepository.updateAuthenticatedUser(updatedProfile)

                if (response != null) {
                    val updatedUserResponse = response

                    val updatedUser = updatedUserResponse.user
                    val newToken = updatedUserResponse.token

                    _userProfile.value = updatedUser
                    Log.d("TokenManagerVIEWMOEDFLDEDED", "Retrieved token: $newToken")

                    tokenManager.saveToken(newToken)

                    _updateResult.value = "Perfil actualizado correctamente"
                } else {
                    _updateResult.value = "Error en la respuesta de la API: Respuesta nula"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al actualizar el perfil: ${e.message}"
                _updateResult.value = "Error al actualizar el perfil: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearUpdateResult() {
        _updateResult.value = ""
    }

    /**
     * Recuperar los datos del usuario desde el repositorio.
     */
    fun fetchUserProfile() {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val user = userRepository.getUserProfile()
                _userProfile.value = user
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar el perfil: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun doLogOut() {
        viewModelScope.launch {
            authRepository.logout()
            tokenManager.clearToken()
        }
    }
}