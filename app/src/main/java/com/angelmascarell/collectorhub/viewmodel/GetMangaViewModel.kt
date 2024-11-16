package com.angelmascarell.collectorhub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angelmascarell.collectorhub.data.model.MangaModel
import com.angelmascarell.collectorhub.data.repository.MangaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetMangaViewModel@Inject constructor(private val mangaApi: MangaRepository) : ViewModel() {
    private val _state = MutableStateFlow<MangaDetailState>(MangaDetailState.Loading)
    val state: StateFlow<MangaDetailState> get() = _state

    fun fetchMangaDetails(mangaId: Long) {
        _state.value = MangaDetailState.Loading
        viewModelScope.launch {
            try {
                val mangaDetails = mangaApi.getMangaDetails(mangaId)
                _state.value = MangaDetailState.Success(mangaDetails)
            } catch (e: Exception) {
                _state.value = MangaDetailState.Error(e.message ?: "Error desconocido")
            }
        }
    }
}

sealed class MangaDetailState {
    object Loading : MangaDetailState()
    data class Success(val mangaDetail: MangaModel) : MangaDetailState()
    data class Error(val message: String) : MangaDetailState()
}