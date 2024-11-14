package com.angelmascarell.collectorhub.home.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angelmascarell.collectorhub.data.local.TokenManager
import com.angelmascarell.collectorhub.data.model.MangaModel
import com.angelmascarell.collectorhub.data.model.ObtainMangaResponse
import com.angelmascarell.collectorhub.data.repository.MangaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mangaRepository: MangaRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    // Estado para manejar la lista de mangas y el estado de la UI
    private val _mangas = mutableStateOf<List<MangaModel>>(emptyList())
    val mangas: State<List<MangaModel>> = _mangas

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    // Función para cargar los mangas
    fun loadMangas() {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                // Verificar el token antes de hacer la petición
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
