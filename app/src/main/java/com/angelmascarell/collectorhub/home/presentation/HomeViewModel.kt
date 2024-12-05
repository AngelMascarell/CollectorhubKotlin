package com.angelmascarell.collectorhub.home.presentation

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angelmascarell.collectorhub.data.local.TokenManager
import com.angelmascarell.collectorhub.data.model.MangaModel
import com.angelmascarell.collectorhub.data.model.ObtainMangaResponse
import com.angelmascarell.collectorhub.data.model.UserModel
import com.angelmascarell.collectorhub.data.repository.MangaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mangaRepository: MangaRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _mangas = mutableStateOf<List<MangaModel>>(emptyList())
    val mangas: State<List<MangaModel>> = _mangas

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    private val _personalizedMangas = mutableStateOf<List<MangaModel>>(emptyList())
    val personalizedMangas: State<List<MangaModel>> = _personalizedMangas

    private val _mangaId = MutableStateFlow<Long?>(null)
    val mangaId: StateFlow<Long?> get() = _mangaId

    fun setMangaId(id: Long) {
        Log.d("HomeViewModel", "Manga ID set: $id")
        _mangaId.value = id
    }

    private val _authenticatedUser = MutableLiveData<UserModel>()
    val authenticatedUser: LiveData<UserModel> get() = _authenticatedUser

    fun fetchAuthenticatedUser() {
        viewModelScope.launch {
            try {
                val user = mangaRepository.getAuthenticatedUser()
                _authenticatedUser.postValue(user)
            } catch (e: Exception) {
                Log.e("YourViewModel", "Error al obtener el usuario autenticado", e)
            }
        }
    }

    fun loadPersonalizedMangas() {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val mangas = mangaRepository.getPersonalizedMangas()
                _personalizedMangas.value = mangas
            } catch (e: Exception) {
                Log.e("MangaViewModel", "Error loading personalized mangas", e)
                _errorMessage.value = "Error al cargar los mangas personalizados"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadMangas() {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val token = tokenManager.getToken()
                if (token.isEmpty()) {
                    _errorMessage.value = "Token no disponible"
                    return@launch
                }

                val response: List<MangaModel> = mangaRepository.getMangas()
                _mangas.value = response

            } catch (e: HttpException) {
                _errorMessage.value = "Error de servidor: ${e.message()}"
            } catch (e: IOException) {
                _errorMessage.value = "Error de red: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }




}


