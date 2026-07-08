package com.chaos.mangaties.presentation.dashboard.bookshelf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chaos.mangaties.domain.model.manga.manga.DownloadedManga
import com.chaos.mangaties.domain.repository.manga.DownloadRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookShelfViewModel @Inject constructor(
    private val downloadRepository: DownloadRepository
) : ViewModel() {
    private val _downloadedMangas = MutableStateFlow<List<DownloadedManga>>(emptyList())
    val downloadedMangas: StateFlow<List<DownloadedManga>> = _downloadedMangas.asStateFlow()

    private val _isDeleting = MutableStateFlow(false)
    val isDeleting: StateFlow<Boolean> = _isDeleting.asStateFlow()


    init {
        loadDownloadedMangas()
    }

    fun loadDownloadedMangas() {
        viewModelScope.launch {
            downloadRepository.getDownloadedManga().collect { mangas->
                _downloadedMangas.value = mangas
            }
        }
    }

    fun deleteDownloadedManga(mangaId: String){
        viewModelScope.launch {
            _isDeleting.value = true
            val result = downloadRepository.deleteDownloadedManga(mangaId)
            _isDeleting.value = false

            if (result.isSuccess){
                loadDownloadedMangas()
            } else{
                result.exceptionOrNull()?.printStackTrace()
            }
        }
    }

    fun deleteAllDownloadedMangas(){
        viewModelScope.launch {
            _isDeleting.value = true
            val result = downloadRepository.deleteAllDownloadedMangas()
            _isDeleting.value = false

            if (result.isSuccess){
                loadDownloadedMangas()
            } else{
                result.exceptionOrNull()?.printStackTrace()
            }
        }
    }

    fun refreshDownloadedMangas() {
        viewModelScope.launch {
            downloadRepository.refreshDownloadedMangas()
            loadDownloadedMangas()
        }
    }
}