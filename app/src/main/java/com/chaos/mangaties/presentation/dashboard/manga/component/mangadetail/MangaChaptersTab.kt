package com.chaos.mangaties.presentation.dashboard.manga.component.mangadetail

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.chaos.mangaties.core.component.button.*
import com.chaos.mangaties.core.component.date.DateFormatter
import com.chaos.mangaties.core.component.text.*
import com.chaos.mangaties.domain.model.manga.manga.MangaChapter
import com.chaos.mangaties.presentation.dashboard.manga.screen.mangadetailscreen.MangaChaptersViewModel

@SuppressLint("RememberReturnType")
@Composable
fun MangaChaptersTab(
    chapters: List<MangaChapter>,
    mangaId: String,
    onLoadMore: () -> Unit,
    onChapterClick: (MangaChapter) -> Unit,
    onReadFirstChapter: () -> Unit,
    onDownloadClick: () -> Unit,
    viewModel: MangaChaptersViewModel = hiltViewModel()
) {

    var isAscending by remember { mutableStateOf(true) }
    val listState = rememberLazyListState()

    val isFavourite by viewModel.isFavourite.collectAsState(initial = false)
    var isHeartActive by remember { mutableStateOf(false) }

    LaunchedEffect(isFavourite) {
        isHeartActive = isFavourite
    }

    LaunchedEffect(mangaId) {
        viewModel.loadFavouriteStatus(mangaId)
    }

    val sortedChapters = remember(chapters, isAscending){
        if (isAscending){
            chapters.sortedBy { it.chapter.toDoubleOrNull() ?: 0.0 }
        } else{
            chapters.sortedByDescending { it.chapter.toDoubleOrNull() ?: 0.0 }
        }
    }

    val shouldLoadMore = remember(listState) {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            totalItems > 0 && lastVisibleItem >= totalItems - 5
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value) {
            onLoadMore()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {

        if (chapters.isEmpty()){
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                AppText(
                    text = "No chapter available",
                    variant = TextState.BodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                item {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    ) {
                        Box(){
                            Icon(
                                imageVector = if (isHeartActive) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = if (isHeartActive) "Remove from favourites" else "Add to favourites",
                                modifier = Modifier
                                    .clickable {
                                        isHeartActive = !isHeartActive
                                        viewModel.toggleFavourite(mangaId)
                                    }
                                    .size(28.dp),
                                tint = if (isHeartActive) Color.Red else MaterialTheme.colorScheme.primary

                            )
                        }
                        Box(){
                            AppButton(
                                text = "Read",
                                onClick = onReadFirstChapter,
                                type = ButtonType.Secondary,
                            )
                        }
                    }
                }
                items(sortedChapters){ chapter ->
                    ChapterItem(
                        chapter = chapter,
                        onClick = { onChapterClick(chapter) }
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
        ) {
            FloatingActionButton(
                onClick = {isAscending = !isAscending},
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = if (isAscending) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                    contentDescription = if (isAscending) "Ascending" else "Descending"
                )
            }

            FloatingActionButton(
                onClick = onDownloadClick,
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            ) {
                Icon(
                    imageVector =  Icons.Default.Download,
                    contentDescription = "Download"
                )
            }
        }
    }
}


@Composable
fun ChapterItem(
    chapter: MangaChapter,
    onClick: () -> Unit
) {
    val formattedDate = remember(chapter.updatedAt) {
        DateFormatter.format(chapter.updatedAt)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppText(
                        text = "Chapter ${chapter.chapter}",
                        variant = TextState.Label,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
                ) {
                AppText(
                    text = "Updated",
                    variant = TextState.Label,
                    color = MaterialTheme.colorScheme.primary
                )
                AppText(
                    text = formattedDate,
                    variant = TextState.Label,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
