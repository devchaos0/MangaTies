package com.chaos.mangaties.presentation.dashboard.bookshelf.screen.chapterscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chaos.mangaties.domain.model.manga.manga.DownloadedChapter
import com.chaos.mangaties.domain.repository.manga.DownloadRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DownloadedChaptersUiState(
    val chapters: List<DownloadedChapter> = emptyList(),
    val isLoading: Boolean = false
)


@HiltViewModel
class DownloadedChaptersViewModel @Inject constructor(
    private val downloadRepository: DownloadRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DownloadedChaptersUiState())
    val uiState: StateFlow<DownloadedChaptersUiState> = _uiState.asStateFlow()

    fun loadChapters(mangaId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            downloadRepository.getDownloadedChapters(mangaId).collect { chapters ->
                _uiState.value = _uiState.value.copy(
                    chapters = chapters,
                    isLoading = false
                )
            }
        }
    }
}