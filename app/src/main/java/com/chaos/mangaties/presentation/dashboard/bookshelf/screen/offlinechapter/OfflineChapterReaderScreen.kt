package com.chaos.mangaties.presentation.dashboard.bookshelf.screen.offlinechapter

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.chaos.mangaties.core.component.text.AppText
import com.chaos.mangaties.core.component.text.TextState
import com.chaos.mangaties.domain.model.manga.manga.DownloadedChapter
import com.chaos.mangaties.navgraph.MangaDataHolder
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfflineChapterReaderScreen(
    chapter: DownloadedChapter,
    allChapters: List<DownloadedChapter>,
    onBack: () -> Unit,
    onChapterChange: (DownloadedChapter) -> Unit,
    viewModel: OfflineChapterReaderViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var areBarsVisible by remember { mutableStateOf(true) }

    LaunchedEffect(chapter.chapterId) {
        viewModel.loadChapterPages(chapter)
    }

    // Use the index from MangaDataHolder to keep track of progress
    val currentIndex = MangaDataHolder.currentChapterIndex

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (areBarsVisible) {
            TopAppBar(
                title = {
                    AppText(
                        text = "Chapter ${chapter.chapterNumber}${chapter.title?.let { " - $it" } ?: ""}",
                        color = Color.White,
                        variant = TextState.HeadingSmall
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black.copy(alpha = 0.8f))
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clickable { areBarsVisible = !areBarsVisible }
        ) {
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else if (uiState.pages.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 80.dp) // Leave space for bottom bar
                ) {
                    items(uiState.pages) { pagePath ->
                        OfflinePageImage(imagePath = pagePath)
                    }
                }
            }
        }

        if (areBarsVisible) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.8f))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        val prev = MangaDataHolder.getPreviousChapter()
                        if (prev != null) {
                            onChapterChange(prev)
                        }
                    },
                    enabled = currentIndex > 0,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (currentIndex > 0) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous")
                    Spacer(modifier = Modifier.width(4.dp))
                    AppText("Previous", variant = TextState.Label, color = MaterialTheme.colorScheme.primary)
                }

                AppText(
                    text = "${currentIndex + 1} / ${allChapters.size}",
                    color = Color.White,
                    variant = TextState.BodySmall
                )

                Button(
                    onClick = {
                        val next = MangaDataHolder.getNextChapter()
                        if (next != null) {
                            onChapterChange(next)
                        }
                    },
                    enabled = currentIndex < allChapters.size - 1,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (currentIndex < allChapters.size - 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary
                    )
                ) {
                    AppText("Next", color = Color.Black, variant = TextState.Label)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next")
                }
            }
        }
    }
}

@Composable
fun OfflinePageImage(imagePath: String) {
    val file = File(imagePath)
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(file)
            .size(Size.ORIGINAL)
            .crossfade(true)
            .build()
    )

    Image(
        painter = painter,
        contentDescription = "Page",
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(4.dp)),
        contentScale = ContentScale.FillWidth
    )
}
