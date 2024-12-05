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
class NewMangasViewModel @Inject constructor(private val mangaApi: MangaRepository) : ViewModel() {

    private val _newMangas = MutableLiveData<List<MangaModel>>()
    val newMangas: LiveData<List<MangaModel>> = _newMangas

    suspend fun loadNewMangas() {
        try {
            val collection: MangaResponseList = mangaApi.getNewMangas()
            val mangaList = collection.mangaResponseList

            _newMangas.postValue(mangaList)

        } catch (e: Exception) {
            _newMangas.postValue(emptyList())
        }
    }



}