package com.angelmascarell.collectorhub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.angelmascarell.collectorhub.data.model.MangaModel
import com.angelmascarell.collectorhub.data.model.MangaResponseList
import com.angelmascarell.collectorhub.data.repository.MangaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class DesiredMangasViewModel @Inject constructor(private val mangaApi: MangaRepository) : ViewModel() {

    private val _userCollection = MutableLiveData<List<MangaModel>>()
    val userCollection: LiveData<List<MangaModel>> = _userCollection


    suspend fun loadUserDesiredCollection() {
        try {
            val collection: MangaResponseList = mangaApi.getUserDesiredMangas()
            val mangaList = collection.mangaResponseList

            _userCollection.postValue(mangaList)

        } catch (e: Exception) {
            _userCollection.postValue(emptyList())
        }
    }


}