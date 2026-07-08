package com.chaos.mangaties.presentation.dashboard.manga.screen.mangareader

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chaos.mangaties.domain.model.manga.manga.MangaChapter
import com.chaos.mangaties.domain.repository.manga.MangaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChapterReaderUiState(
    val pages: List<String> = emptyList(),
    val baseUrl: String = "",
    val hash: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val allChapters: List<MangaChapter> = emptyList()
)

@HiltViewModel
class ChapterReaderViewModel @Inject constructor(
    private val repository: MangaRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(ChapterReaderUiState())
    val uiState: StateFlow<ChapterReaderUiState> = _uiState.asStateFlow()

    // Function to update the chapter list from the UI
    fun setChapters(chapters: List<MangaChapter>) {
        _uiState.update { it.copy(allChapters = chapters) }
    }

    fun loadChapterPages (chapterId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            val result = repository.getChapterPages(chapterId)

            result.fold(
                onSuccess = { chapterPages ->
                    _uiState.value = _uiState.value.copy(
                        pages = chapterPages.data,
                        baseUrl = chapterPages.baseUrl,
                        hash = chapterPages.hash,
                        isLoading = false,
                        error = null
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Failed to load pages"
                    )
                }
            )

        }
    }

}