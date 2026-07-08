package com.chaos.mangaties.presentation.dashboard.bookshelf.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.chaos.mangaties.core.component.text.*
import com.chaos.mangaties.domain.model.manga.manga.DownloadedManga

@Composable
fun Downloaded(
    downloadedMangas: List<DownloadedManga>,
    onMangaClick: (String, String) -> Unit,
    onDeleteManga: (String) -> Unit,
    modifier: Modifier = Modifier,
    selectedMangaIds: List<String> = emptyList(), // Added
    isSelectionMode: Boolean = false // Added
) {
    if (downloadedMangas.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = "No downloads",
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                AppText(
                    text = "No downloaded manga",
                    variant = TextState.BodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                AppText(
                    text = "Download chapters to read offline",
                    variant = TextState.BodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(downloadedMangas) { manga ->
                val isSelected = selectedMangaIds.contains(manga.mangaId)
                DownloadedMangaItem(
                    manga = manga,
                    isSelected = isSelected,
                    isSelectionMode = isSelectionMode,
                    onClick = { onMangaClick(manga.mangaId, manga.mangaTitle) },
                    onDelete = { onDeleteManga(manga.mangaId) }
                )
            }
        }
    }
}

@Composable
fun DownloadedMangaItem(
    manga: DownloadedManga,
    isSelected: Boolean,
    isSelectionMode: Boolean,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .then(
                if (isSelected) Modifier.border(2.dp, MaterialTheme.colorScheme.error, RoundedCornerShape(12.dp))
                else Modifier
            ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            val coverUrl = manga.coverFileName?.let { fileName ->
                "https://uploads.mangadex.org/covers/${manga.mangaId}/$fileName"
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.7f)
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                if (coverUrl != null) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(coverUrl)
                                .crossfade(true)
                                .build()
                        ),
                        contentDescription = manga.mangaTitle,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(48.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                
                if (!isSelectionMode) {
                    IconButton(
                        onClick = { showDeleteDialog = true },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.error.copy(alpha = 0.8f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.onError
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                AppText(
                    text = manga.mangaTitle,
                    variant = TextState.BodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )
                AppText(
                    text = "${manga.downLoadedChapters.size} chapters",
                    variant = TextState.Label,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                AppText(
                    text = "Delete ${manga.mangaTitle}?",
                    variant = TextState.HeadingMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            text = {
                AppText(
                    text = "This will delete all downloaded chapters of this manga. This action cannot be undone.",
                    variant = TextState.BodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDelete()
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    AppText(text = "Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    AppText(
                        text = "Cancel",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        )
    }
}
