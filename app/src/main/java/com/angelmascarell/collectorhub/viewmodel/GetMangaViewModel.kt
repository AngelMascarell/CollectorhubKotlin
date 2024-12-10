package com.angelmascarell.collectorhub.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angelmascarell.collectorhub.data.model.MangaModel
import com.angelmascarell.collectorhub.data.model.RateCreateModel
import com.angelmascarell.collectorhub.data.model.RateModel
import com.angelmascarell.collectorhub.data.model.RateResponseList
import com.angelmascarell.collectorhub.data.repository.MangaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class GetMangaViewModel @Inject constructor(private val mangaApi: MangaRepository) : ViewModel() {

    private val _state = MutableStateFlow<MangaDetailState>(MangaDetailState.Loading)
    val state: StateFlow<MangaDetailState> get() = _state

    private var resultMessage: String? = null
        private set

    private var isLoading: Boolean = false
        private set

    private var errorMessage: String? = null
        private set

    suspend fun checkTasks(): Response<String> {
        isLoading = true
        return try {
            val response = mangaApi.checkTaskCompletion()
            isLoading = false
            response
        } catch (e: Exception) {
            isLoading = false
            Response.error(500, "Error al realizar la llamada".toResponseBody())
        }
    }


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
                val response: RateResponseList = mangaApi.getRatesByMangaId(mangaId)
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

    private val _reviewState = MutableLiveData<ReviewState>()
    val reviewState: LiveData<ReviewState> get() = _reviewState

    fun submitReview(review: RateCreateModel) {
        viewModelScope.launch {
            _reviewState.value = ReviewState.Loading
            try {
                val response = mangaApi.addReview(review)
                if (response.isSuccessful) {
                    _reviewState.value = ReviewState.Success("Reseña enviada con éxito")
                    _hasReviewed.value = true
                } else {
                    _reviewState.value = ReviewState.Error("Error al enviar la reseña")
                }
            } catch (e: Exception) {
                _reviewState.value = ReviewState.Error("Error al enviar la reseña")
            }
        }
    }


    private val _hasReviewed = MutableStateFlow<Boolean?>(null)
    val hasReviewed: StateFlow<Boolean?> get() = _hasReviewed

    fun checkUserReview(mangaId: Long) {
        viewModelScope.launch {
            try {
                val isReviewed = mangaApi.hasUserReviewed(mangaId)
                _hasReviewed.value = isReviewed
            } catch (e: Exception) {
                _hasReviewed.value = null
            }
        }
    }

    private val _searchResult = MutableStateFlow<MangaModel?>(null)
    val searchResult: StateFlow<MangaModel?> = _searchResult

    fun searchMangaByName(name: String, onResult: (Long?) -> Unit) {
        viewModelScope.launch {
            val manga = mangaApi.getMangaByTitle(name)
            onResult(manga?.id)
        }
    }

    suspend fun isMangaInDesiredCollection(mangaId: Long): Boolean {
        return mangaApi.checkIfMangaInDesiredCollection(mangaId)
    }

    suspend fun addDesiredMangaToUser(mangaId: Long): Response<String> {
        return try {
            val response = mangaApi.addDesiredMangaToUser(mangaId)
            response
        } catch (e: Exception) {
            Response.success("ERROR")
        }
    }


    sealed class ReviewState {
        object Loading : ReviewState()
        data class Success(val message: String) : ReviewState()
        data class Error(val error: String) : ReviewState()
        data class UserReviewed(val isReviewed: Boolean) : ReviewState()
    }

    sealed class MangaDetailState {
        object Loading : MangaDetailState()
        data class Success(val mangaDetail: MangaModel) : MangaDetailState()
        data class Error(val message: String) : MangaDetailState()
    }
}