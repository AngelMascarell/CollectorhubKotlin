package com.angelmascarell.collectorhub.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angelmascarell.collectorhub.data.model.MangaModel
import com.angelmascarell.collectorhub.data.repository.MangaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.angelmascarell.collectorhub.data.model.RateModel
import com.angelmascarell.collectorhub.data.model.RateResponseList
import retrofit2.Response


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

    private val _averageRate: MutableState<Int> = mutableStateOf(0)
    val averageRate: State<Int> = _averageRate


    fun fetchAverageRate(mangaId: Long) {
        viewModelScope.launch {
            try {
                val rate = mangaApi.getAverageRateByMangaId(mangaId)
                _averageRate.value = rate
            } catch (e: Exception) {
                _averageRate.value = 0
            }
        }
    }

    private val _rates = MutableStateFlow<List<RateModel>>(emptyList())
    val rates: StateFlow<List<RateModel>> get() = _rates

    fun fetchRatesByMangaId(mangaId: Long) {
        viewModelScope.launch {
            try {
                // Llamada a la API que devuelve RateResponseList
                val response: RateResponseList = mangaApi.getRatesByMangaId(mangaId)
                // Mapeo de RateResponse a RateModel
                _rates.value = response.rateResponseList.map { rateResponse ->
                    RateModel(
                        id = rateResponse.id,
                        userId = 1,
                        mangaId = 1,
                        rate = rateResponse.rate,
                        comment = rateResponse.comment,
                        date = rateResponse.date
                    )
                }
            } catch (e: Exception) {
                // Manejar errores y establecer la lista vacía
                Log.e("RateViewModel", "Error fetching rates", e)
                _rates.value = emptyList()
            }
        }
    }

    suspend fun addMangaToUser(mangaId: Long): Response<String> {
        return try {
            val response = mangaApi.addMangaToUser(mangaId)
            response
        } catch (e: Exception) {
            Response.success("FALLO ")
        }
    }

    suspend fun isMangaInCollection(mangaId: Long): Boolean {
        return mangaApi.checkIfMangaInCollection(mangaId)
    }
}

sealed class MangaDetailState {
    object Loading : MangaDetailState()
    data class Success(val mangaDetail: MangaModel) : MangaDetailState()
    data class Error(val message: String) : MangaDetailState()
}