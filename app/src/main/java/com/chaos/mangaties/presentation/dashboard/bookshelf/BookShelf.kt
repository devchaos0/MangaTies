package com.chaos.mangaties.presentation.dashboard.bookshelf

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.chaos.mangaties.core.component.text.*
import com.chaos.mangaties.presentation.dashboard.bookshelf.component.Downloaded
import com.chaos.mangaties.presentation.dashboard.bookshelf.component.Favourite

enum class BookShelf {
    FAVOURITE,
    DOWNLOADED
}

@Composable
fun BookShelfScreen(
    onDownloadedMangaClick: (String, String) -> Unit,
    onFavouriteMangaClick: (String) -> Unit,
    viewModel: BookShelfViewModel = hiltViewModel()
) {
    var selectedTab by remember { mutableStateOf(BookShelf.FAVOURITE) }
    val downloadedState by viewModel.downloadedMangas.collectAsState()
    
    // State for multi-selection mode
    var isSelectionMode by remember { mutableStateOf(false) }
    val selectedMangaIds = remember { mutableStateListOf<String>() }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTab.ordinal) {
            Tab(
                selected = selectedTab == BookShelf.FAVOURITE,
                onClick = { selectedTab = BookShelf.FAVOURITE },
                text = { AppText(text = "Favourite", color = MaterialTheme.colorScheme.primary) }
            )
            Tab(
                selected = selectedTab == BookShelf.DOWNLOADED,
                onClick = { selectedTab = BookShelf.DOWNLOADED },
                text = { AppText(text = "Downloaded (${downloadedState.size})", color = MaterialTheme.colorScheme.primary) }
            )
        }

        Box(modifier = Modifier.fillMaxSize().weight(1f)) {
            when (selectedTab) {
                BookShelf.FAVOURITE -> {
                    Favourite(
                        onFavouriteClick = onFavouriteMangaClick
                    )
                }
                BookShelf.DOWNLOADED -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        if (downloadedState.isNotEmpty()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                // Toggle Selection Mode
                                Row(
                                    modifier = Modifier.clickable { 
                                        isSelectionMode = !isSelectionMode
                                        if (!isSelectionMode) selectedMangaIds.clear()
                                    },
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if (isSelectionMode) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                                        contentDescription = "Select Mode",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    AppText(
                                        text = if (isSelectionMode) "Cancel Selection" else "Select Multiple",
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }

                                // Delete Action (only if items selected)
                                if (isSelectionMode && selectedMangaIds.isNotEmpty()) {
                                    IconButton(onClick = { showDeleteDialog = true }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete Selected", tint = MaterialTheme.colorScheme.error)
                                    }
                                }
                            }
                        }
                        
                        Downloaded(
                            downloadedMangas = downloadedState,
                            selectedMangaIds = selectedMangaIds,
                            isSelectionMode = isSelectionMode,
                            onMangaClick = { mangaId, mangaTitle ->
                                if (isSelectionMode) {
                                    if (selectedMangaIds.contains(mangaId)) selectedMangaIds.remove(mangaId)
                                    else selectedMangaIds.add(mangaId)
                                } else {
                                    onDownloadedMangaClick(mangaId, mangaTitle)
                                }
                            },
                            onDeleteManga = { mangaId -> viewModel.deleteDownloadedManga(mangaId) }
                        )
                    }
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { AppText(text = "Delete Selected?", variant = TextState.HeadingMedium, color = MaterialTheme.colorScheme.primary) },
            confirmButton = {
                TextButton(onClick = {
                    selectedMangaIds.forEach { viewModel.deleteDownloadedManga(it) }
                    selectedMangaIds.clear()
                    isSelectionMode = false
                    showDeleteDialog = false
                }) { AppText(text = "Delete", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = { TextButton(onClick = { showDeleteDialog = false }) { AppText(text = "Cancel", color = MaterialTheme.colorScheme.primary) } }
        )
    }
}
