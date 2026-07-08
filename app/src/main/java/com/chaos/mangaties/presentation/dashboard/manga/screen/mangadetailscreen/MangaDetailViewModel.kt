package com.chaos.mangaties.presentation.dashboard.manga.screen.mangadetailscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chaos.mangaties.domain.model.manga.manga.MangaChapter
import com.chaos.mangaties.domain.model.manga.manga.MangaDetails
import com.chaos.mangaties.domain.repository.manga.MangaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MangaDetailUiState(
    val mangaDetails: MangaDetails? = null,
    val mangaChapters: List<MangaChapter> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingChapters: Boolean = false,
    val error: String? = null,
    val canLoadMore: Boolean = true

)

@HiltViewModel
class MangaDetailViewModel @Inject constructor(
    private val repository: MangaRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MangaDetailUiState())
    val uiState: StateFlow<MangaDetailUiState> = _uiState.asStateFlow()

    fun loadMangaDetails(mangaId: String){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = repository.getMangaDetails(mangaId)

            result.fold(
                onSuccess = { mangaDetails ->
                    _uiState.update { it.copy(
                        mangaDetails = mangaDetails,
                        isLoading = false,
                        error = null
                    ) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(
                        isLoading = false,
                        error = error.message ?: "Failed to load manga details"
                    ) }
                }
            )
        }
    }

    fun loadMangaChapter(mangaId: String, offset: Int = 0, limit: Int = 100){
        viewModelScope.launch {
            // Only set loading if it's the initial load
            if (offset == 0) {
                _uiState.update { it.copy(isLoading = true) }
            }

            val result = repository.getMangaChapters(mangaId, offset = offset, limit = limit)

            result.fold(
                onSuccess = { chapters ->
                    val currentChapters = if (offset == 0) {
                        chapters
                    } else {
                        _uiState.value.mangaChapters + chapters
                    }

                    _uiState.value = _uiState.value.copy(
                        mangaChapters = currentChapters,
                        isLoadingChapters = false,
                        canLoadMore = chapters.size == limit
                    )
                },
                onFailure = { error ->
                    _uiState.update { it.copy(
                        isLoading = false,
                        error = error.message ?: error.localizedMessage
                    ) }
                }
            )
        }
    }
}
