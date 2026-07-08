package com.chaos.mangaties.presentation.dashboard.manga.screen.mangadownload

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chaos.mangaties.data.remote.repository.MangaMetadata
import com.chaos.mangaties.domain.model.manga.manga.MangaChapter
import com.chaos.mangaties.domain.repository.manga.DownloadRepository
import com.chaos.mangaties.domain.repository.manga.MangaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.io.File
import javax.inject.Inject

data class DownloadChaptersStatus(
    val chapter: MangaChapter,
    val isSelected: Boolean = false,
    val isDownloaded: Boolean = false
)

data class DownloadChaptersUiState(
    val mangaId: String = "",
    val mangaTitle: String = "",
    val chapterStatuses: List<DownloadChaptersStatus> = emptyList(),
    val selectedChapters: List<MangaChapter> = emptyList(),
    val isDownloading: Boolean = false,
    val downloadProgress: Float = 0f,
    val currentDownloadingChapter: String? = null,
    val allSelected: Boolean = false
)

@HiltViewModel
class DownloadChaptersViewModel @Inject constructor(
    private val downloadRepository: DownloadRepository,
    private val mangaRepository: MangaRepository,
    @param:ApplicationContext private val context: Context
) : ViewModel(){

    private val _uiState = MutableStateFlow(DownloadChaptersUiState())
    val uiState: StateFlow<DownloadChaptersUiState> = _uiState.asStateFlow()

    private var chaptersMap = mutableMapOf<String, MangaChapter>()

    fun initialize(mangaId: String, mangaTitle: String, chapters: List<MangaChapter>){
        viewModelScope.launch {
            chaptersMap = chapters.associateBy { it.id }.toMutableMap()

            val statues = chapters.map { chapter ->
                val isDownloaded = downloadRepository.isChapterDownloaded((chapter.id))

                DownloadChaptersStatus(
                    chapter = chapter,
                    isSelected = false,
                    isDownloaded = isDownloaded
                )
            }

            _uiState.update {
                it.copy(
                    mangaId = mangaId,
                    mangaTitle = mangaTitle,
                    chapterStatuses = statues,
                    selectedChapters = emptyList()
                )
            }
            updateAllSelectedState()
        }
    }

    fun toggleChapterSelection(chapterId: String){
        val updatedStatues = _uiState.value.chapterStatuses.map { status ->
            if (status.chapter.id == chapterId && !status.isDownloaded){
                status.copy(isSelected = !status.isSelected)
            }else{
                status
            }
        }

        val selected = updatedStatues.filter { it.isSelected }.map { it.chapter }

        _uiState.update {
            it.copy(
                chapterStatuses = updatedStatues,
                selectedChapters = selected
            )
        }
        updateAllSelectedState()
    }

    fun toggleSelectAll(){
        val allSelected = _uiState.value.allSelected
        val updatedStatues = _uiState.value.chapterStatuses.map { status ->
            if (!status.isDownloaded) {
                status.copy(isSelected = !allSelected)
            }else {
                status
            }
        }

        val selected = updatedStatues.filter { it.isSelected }.map { it.chapter }

        _uiState.update {
            it.copy(
                chapterStatuses = updatedStatues,
                selectedChapters = selected,
                allSelected = !allSelected
            )
        }

    }

    private fun updateAllSelectedState(){
        val selectable = _uiState.value.chapterStatuses.filter { !it.isDownloaded }

        val selected = selectable.filter { it.isSelected }

        _uiState.update {
            it.copy(
                allSelected = selectable.isNotEmpty() && selected.size == selectable.size
            )
        }
    }

    fun startDownload() {
        viewModelScope.launch {
            _uiState.update { it.copy(isDownloading = true, downloadProgress = 0f) }

            val selectedChapters = _uiState.value.selectedChapters
            val totalChapters = selectedChapters.size
            val mangaId = _uiState.value.mangaId
            val mangaTitle = _uiState.value.mangaTitle
            
            selectedChapters.forEachIndexed { index, chapter ->
                _uiState.update {
                    it.copy(
                        currentDownloadingChapter = chapter.chapter,
                        downloadProgress = index.toFloat() / totalChapters
                    )
                }

                val pagesResult = mangaRepository.getChapterPages(chapter.id)
                pagesResult.onSuccess { chapterPages ->
                    val pageUrls = chapterPages.data.map { fileName ->
                        "${chapterPages.baseUrl}/data/${chapterPages.hash}/$fileName"
                    }
                    
                    val mangaDetail = mangaRepository.getMangaDetails(mangaId)
                    val coverFileName = mangaDetail.getOrNull()?.coverFileName
                    
                    val downloadDir = File(context.filesDir, "downloads")
                    val mangaDir = File(downloadDir, "${mangaId}_${mangaTitle.replace(" ", "_")}")
                    if (!mangaDir.exists()) {
                        mangaDir.mkdirs()
                    }
                    val mangaMetadataFile = File(mangaDir, "manga_metadata.json")
                    if (!mangaMetadataFile.exists()) {
                        val metadata = MangaMetadata(
                            mangaId = mangaId,
                            mangaTitle = mangaTitle,
                            coverFileName = coverFileName
                        )
                        mangaMetadataFile.writeText(Json.encodeToString(MangaMetadata.serializer(), metadata))
                    }

                    downloadRepository.downloadChapter(
                        mangaId = mangaId,
                        mangaTitle = mangaTitle,
                        chapterId = chapter.id,
                        chapterNumber = chapter.chapter,
                        title = chapter.title,
                        volume = chapter.volume,
                        pageUrls = pageUrls,
                        onProgress = { progress ->
                            _uiState.update {
                                it.copy(
                                    downloadProgress = (index.toFloat() + progress) / totalChapters
                                )
                            }
                        }
                    )
                }
            }

            val refreshedStatues = _uiState.value.chapterStatuses.map { status ->
                val isDownloaded = downloadRepository.isChapterDownloaded(status.chapter.id)

                status.copy(
                    isDownloaded = isDownloaded,
                    isSelected = false
                )
            }

            _uiState.update {
                it.copy (
                    chapterStatuses = refreshedStatues,
                    selectedChapters = emptyList(),
                    isDownloading = false,
                    downloadProgress = 1f,
                    currentDownloadingChapter = null
                )
            }
            updateAllSelectedState()
        }
    }

}