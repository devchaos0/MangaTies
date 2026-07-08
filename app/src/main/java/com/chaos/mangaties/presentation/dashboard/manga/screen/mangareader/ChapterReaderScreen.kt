package com.chaos.mangaties.presentation.dashboard.manga.screen.mangareader

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.chaos.mangaties.core.component.text.*
import com.chaos.mangaties.domain.model.manga.manga.MangaChapter

@Composable
fun ChapterReaderScreen(
    chapterId: String,
    initialChapters: List<MangaChapter>,
    onBack: () -> Unit,
    onChapterChange: (String) -> Unit,
    viewModel: ChapterReaderViewModel = hiltViewModel()
) {
    LaunchedEffect(initialChapters) {
        viewModel.setChapters(initialChapters)
    }

    val uiState by viewModel.uiState.collectAsState()
    val allChapters = uiState.allChapters

    val currentChapterIndex = remember(allChapters, chapterId) {
        val index = allChapters.indexOfFirst { it.id == chapterId}
        Log.d("ChapterReader", "ChapterId: $chapterId, Found Index: $index, AllChapters size: ${allChapters.size}")
        if (index == -1 && allChapters.isNotEmpty()) 0 else index
    }

    var areBarsVisible by remember { mutableStateOf(true) }

    LaunchedEffect(chapterId) {
        viewModel.loadChapterPages(chapterId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (areBarsVisible) {
            ChapterReaderTopBar(
                chapter = if (currentChapterIndex >= 0) allChapters.getOrNull(currentChapterIndex) else null,
                onBack = onBack
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clickable { areBarsVisible = !areBarsVisible }
        ) {
            when {
                uiState.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
                uiState.error != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            AppText("Error loading pages", color = Color.White, variant = TextState.BodyLarge)
                            AppText(uiState.error ?: "Unknown error", color = Color.White.copy(alpha = 0.7f), variant = TextState.BodyMedium)
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { viewModel.loadChapterPages(chapterId) }) { AppText("Retry") }
                        }
                    }
                }
                uiState.pages.isNotEmpty() -> {
                    ChapterPagesView(
                        pages = uiState.pages,
                        baseUrl = uiState.baseUrl,
                        hash = uiState.hash,
                        onPageTap = { areBarsVisible = !areBarsVisible }
                    )
                }
            }
        }

        if (areBarsVisible) {
            ChapterReaderBottomBar(
                currentIndex = if (currentChapterIndex < 0) 0 else currentChapterIndex,
                totalChapters = allChapters.size.coerceAtLeast(1),
                onPrevious = {
                    if (currentChapterIndex > 0) {
                        onChapterChange(allChapters[currentChapterIndex - 1].id)
                    }
                },
                onNext = {
                    if (currentChapterIndex >= 0 && currentChapterIndex < allChapters.size - 1) {
                        onChapterChange(allChapters[currentChapterIndex + 1].id)
                    }
                },
                canGoPrevious = currentChapterIndex > 0,
                canGoNext = currentChapterIndex >= 0 && currentChapterIndex < allChapters.size - 1
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChapterReaderTopBar(chapter: MangaChapter?, onBack: () -> Unit) {
    TopAppBar(
        title = {
            AppText(
                text = chapter?.let { "Chapter ${it.chapter}${it.title?.let { t -> " - $t" } ?: ""}" } ?: "Reading",
                color = Color.White,
                variant = TextState.DisplaySmall
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

@Composable
fun ChapterReaderBottomBar(currentIndex: Int, totalChapters: Int, onPrevious: () -> Unit, onNext: () -> Unit, canGoPrevious: Boolean, canGoNext: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth().background(Color.Black.copy(alpha = 0.8f)).padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(onClick = onPrevious, enabled = canGoPrevious, colors = ButtonDefaults.buttonColors(containerColor = if (canGoPrevious) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.secondaryContainer)) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous", modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(4.dp))
            AppText("Previous", variant = TextState.Label, color = MaterialTheme.colorScheme.primary)
        }
        AppText("${currentIndex + 1} / $totalChapters", color = Color.White, variant = TextState.BodySmall)
        Button(onClick = onNext, enabled = canGoNext, colors = ButtonDefaults.buttonColors(containerColor = if (canGoNext) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary)) {
            AppText("Next", variant = TextState.Label, color = Color.Black)
            Spacer(modifier = Modifier.width(4.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next", modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
fun ChapterPagesView(pages: List<String>, baseUrl: String, hash: String, onPageTap: () -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        items(pages) { pageFileName ->
            ChapterPageImage(imageUrl = "$baseUrl/data/$hash/$pageFileName", onTap = onPageTap)
        }
    }
}

@Composable
fun ChapterPageImage(imageUrl: String, modifier: Modifier = Modifier, onTap: () -> Unit) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current).data(imageUrl).size(Size.ORIGINAL).crossfade(true).build()
    )
    Image(
        painter = painter,
        contentDescription = "Page",
        modifier = modifier.fillMaxWidth().wrapContentHeight().clip(RoundedCornerShape(4.dp)).clickable { onTap() },
        contentScale = ContentScale.FillWidth
    )
}