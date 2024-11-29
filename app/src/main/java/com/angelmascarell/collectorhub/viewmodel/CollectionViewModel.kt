package com.angelmascarell.collectorhub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.angelmascarell.collectorhub.data.model.MangaModel
import com.angelmascarell.collectorhub.data.model.MangaResponseList
import com.angelmascarell.collectorhub.data.repository.MangaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(private val mangaRepository: MangaRepository) : ViewModel() {

    private val _userCollection = MutableLiveData<List<MangaModel>>()
    val userCollection: LiveData<List<MangaModel>> = _userCollection

    private val _genresCount = MutableLiveData<Map<String, Int>>()
    val genresCount: LiveData<Map<String, Int>> = _genresCount

    private val _totalMangas = MutableLiveData<Int>()
    val totalMangas: LiveData<Int> = _totalMangas

    private fun getGenreName(genreId: Long): String {
        val genreMap = mapOf(
            1L to "Action",
            2L to "Comedy",
            3L to "Shonen",
            4L to "Sports",
            5L to "Shojo",
            6L to "Josei",
            8L to "Thriller",
            9L to "Oneshot",
            10L to "Psychological",
            11L to "Horror",
            12L to "Mystery"
        )
        return genreMap[genreId] ?: "Desconocido"
    }

    suspend fun loadUserCollection() {
        try {
            val collection: MangaResponseList = mangaRepository.getUserMangas()
            val mangaList = collection.mangaResponseList

            _userCollection.postValue(mangaList)

            val genreCountMap = mangaList
                .groupingBy { getGenreName(it.genreId) }
                .eachCount()

            _genresCount.postValue(genreCountMap)
            _totalMangas.postValue(mangaList.size)
        } catch (e: Exception) {
            _userCollection.postValue(emptyList())
        }
    }
}


