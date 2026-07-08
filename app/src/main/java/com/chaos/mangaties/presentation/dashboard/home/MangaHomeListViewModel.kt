package com.chaos.mangaties.presentation.dashboard.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chaos.mangaties.domain.model.manga.home.MangaHome
import com.chaos.mangaties.domain.repository.manga.MangaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MangaUiState(
    val mangaList: List<MangaHome> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class MangaHomeListViewModel @Inject constructor(
    private val repository: MangaRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(MangaUiState())
    val uiState: StateFlow<MangaUiState> = _uiState.asStateFlow()

    private var currentOffset = 0
    private val pageSize = 10
    private var canLoadMore = true

    init {
        loadMoreManga()
    }

    fun loadMoreManga(){
        if (_uiState.value.isLoading || !canLoadMore) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val result = repository.getMangaList(currentOffset,pageSize)


            result.fold(
                onSuccess = { newManga ->
                    val updatedList = _uiState.value.mangaList + newManga
                    _uiState.value = _uiState.value.copy(
                        mangaList = updatedList,
                        isLoading = false
                    )
                    println(updatedList)
                    currentOffset += pageSize
                    canLoadMore = newManga.size == pageSize
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        error = error.message,
                        isLoading = false
                    )
                    println(error)
                }
            )
        }
    }

    fun clearError(){
        _uiState.value = _uiState.value.copy(error = null)
    }
}