package com.chaos.mangaties.presentation.dashboard.manga.screen.mangadetailscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.chaos.mangaties.core.component.button.AppButton
import com.chaos.mangaties.core.component.text.AppText
import com.chaos.mangaties.domain.model.manga.manga.MangaChapter
import com.chaos.mangaties.presentation.dashboard.manga.component.mangadetail.MangaDetailContent

@Composable
fun MangaDetailsScreen(
    mangaId: String,
    onBack: () -> Unit,
    viewModel: MangaDetailViewModel = hiltViewModel(),
    onNavigateToChapterReader: (String, List<MangaChapter>) -> Unit,
    onNavigateToDownload: (String, String, String?, List<MangaChapter>) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    var currentOffset by remember { mutableIntStateOf(0) }

    LaunchedEffect(mangaId) {
        viewModel.loadMangaDetails(mangaId)
        viewModel.loadMangaChapter(mangaId, offset = 0, limit = 100)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->

        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            when{
                uiState.isLoading && uiState.mangaDetails == null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        CircularProgressIndicator()
                    }
                }

                uiState.error != null && uiState.mangaDetails == null ->{
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AppText(
                                "Error loading manga details",
                                color = MaterialTheme.colorScheme.error
                            )
                            AppText(
                                text = uiState.error!!,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(16.dp)
                            )
                            AppButton(
                                "Retry",
                                onClick = { viewModel.loadMangaDetails(mangaId) }
                            )
                        }
                    }
                }

                uiState.mangaDetails != null ->{
                    val mangaTitle = uiState.mangaDetails?.title ?: "Manga"
                    val coverUrl = uiState.mangaDetails?.coverFileName?.let { fileName ->
                        "https://uploads.mangadex.org/covers/${uiState.mangaDetails!!.id}/$fileName"
                    }

                    MangaDetailContent(
                        mangaDetail = uiState.mangaDetails,
                        mangaChapter = uiState.mangaChapters,
                        onLoadMoreChapters = {
                            currentOffset += 100
                            viewModel.loadMangaChapter(mangaId, offset = currentOffset, limit = 100)
                        },
                        onChapterClick = { chapter ->
                            onNavigateToChapterReader(chapter.id, uiState.mangaChapters)
                        },
                        onReadFirstChapter = {
                            uiState.mangaChapters.firstOrNull()?.let { firstChapter ->
                                onNavigateToChapterReader(firstChapter.id, uiState.mangaChapters)
                            }
                        },
                        onDownloadClick = {
                            val details = uiState.mangaDetails
                            if (details != null && uiState.mangaChapters.isNotEmpty()) {
                                val title = details.title ?: "Manga"
                                val coverUrl = details.coverFileName?.let { fileName ->
                                    "https://uploads.mangadex.org/covers/$mangaId/$fileName"
                                }
                                onNavigateToDownload(
                                    mangaId,
                                    title,
                                    coverUrl,
                                    uiState.mangaChapters
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}
