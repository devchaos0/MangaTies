package com.chaos.mangaties.presentation.dashboard.bookshelf.screen.chapterscreen


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chaos.mangaties.core.component.text.AppText
import com.chaos.mangaties.core.component.text.TextState
import com.chaos.mangaties.domain.model.manga.manga.DownloadedChapter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadedChaptersScreen(
    mangaId: String,
    mangaTitle: String,
    onBack: () -> Unit,
    onChapterClick: (DownloadedChapter) -> Unit,
    viewModel: DownloadedChaptersViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(mangaId) {
        viewModel.loadChapters(mangaId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    AppText(
                        text = mangaTitle,
                        variant = TextState.HeadingMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (uiState.chapters.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    AppText(
                        text = "No downloaded chapters",
                        variant = TextState.BodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.chapters) { chapter ->
                        DownloadedChapterItem(
                            chapter = chapter,
                            onClick = { onChapterClick(chapter) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DownloadedChapterItem(
    chapter: DownloadedChapter,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                AppText(
                    text = "Chapter ${chapter.chapterNumber}",
                    variant = TextState.BodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                chapter.title?.let {
                    AppText(
                        text = it,
                        variant = TextState.BodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.Download,
                contentDescription = "Downloaded",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}