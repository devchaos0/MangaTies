package com.chaos.mangaties.presentation.dashboard.bookshelf.screen.offlinechapter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chaos.mangaties.domain.model.manga.manga.DownloadedChapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OfflineChapterReaderUiState(
    val pages: List<String> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class OfflineChapterReaderViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(OfflineChapterReaderUiState())
    val uiState: StateFlow<OfflineChapterReaderUiState> = _uiState.asStateFlow()

    fun loadChapterPages(chapter: DownloadedChapter) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true
            )

            // Pages are already stored in the chapter
            _uiState.value = _uiState.value.copy(
                pages = chapter.pages,
                isLoading = false
            )
        }
    }
}