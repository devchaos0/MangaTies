package com.chaos.mangaties.presentation.dashboard.manga.screen.mangadownload

import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.chaos.mangaties.core.component.button.*
import com.chaos.mangaties.core.component.text.*
import com.chaos.mangaties.domain.model.manga.manga.MangaChapter
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.platform.LocalContext
import com.chaos.mangaties.presentation.dashboard.manga.component.mangadowload.DownloadChapterItem
import com.chaos.mangaties.presentation.dashboard.manga.component.mangadowload.DownloadProgressOverlay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadChaptersScreen(
    mangaId: String,
    mangaTitle: String,
    chapters: List<MangaChapter>,
    onBack: () -> Unit,
    viewModel: DownloadChaptersViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(mangaId, chapters) {
        viewModel.initialize(mangaId, mangaTitle, chapters)
    }

    LaunchedEffect(uiState.isDownloading) {
        if (!uiState.isDownloading && uiState.downloadProgress >= 1f && uiState.selectedChapters.isEmpty()) {
            // Download completed, show success message
            Toast.makeText(context, "Download completed!", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    AppText(
                        text = "Download Chapters",
                        variant = TextState.HeadingMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = { viewModel.toggleSelectAll() }
                    ) {
                        AppText(
                            text = if (uiState.allSelected) "Deselect All" else "Select All",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        bottomBar = {
            if (uiState.selectedChapters.isNotEmpty()){
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shadowElevation = 8.dp,
                    tonalElevation = 3.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        AppText(
                            text = "${uiState.selectedChapters.size} chapters selected",
                            variant = TextState.BodyMedium
                        )
                        AppButton(
                            text = if (uiState.isDownloading) "Downloading..." else "Download (${uiState.selectedChapters.size})",
                            onClick = { viewModel.startDownload() },
                            type = ButtonType.Primary,
                            enabled = uiState.selectedChapters.isNotEmpty() && !uiState.isDownloading
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ){
            if (uiState.isDownloading){
                DownloadProgressOverlay(
                    progress = uiState.downloadProgress,
                    currentChapter = uiState.currentDownloadingChapter
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.chapterStatuses){ status ->
                    DownloadChapterItem(
                        chapter = status.chapter,
                        isSelected = status.isSelected,
                        isDownloaded = status.isDownloaded,
                        onToggle = { viewModel.toggleChapterSelection(status.chapter.id) }

                    )
                }
            }
        }
    }

}




