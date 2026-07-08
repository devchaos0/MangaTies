package com.chaos.mangaties.presentation.dashboard.discover

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.chaos.mangaties.core.component.input.AppTextField
import com.chaos.mangaties.core.component.text.*
import kotlinx.coroutines.flow.*
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.chaos.mangaties.domain.model.manga.manga.SearchManga

@Composable
fun DiscoverScreen(
    onMangaClick: (String) -> Unit,
    viewModel: DiscoverViewModel = hiltViewModel()
) {

    val search = rememberTextFieldState()
    val uiState by viewModel.uiState.collectAsState()
    val gridState = rememberLazyGridState()

    LaunchedEffect(Unit) {
        snapshotFlow { search.text.toString() }
            .distinctUntilChanged()
            .collect { query ->
                viewModel.updateSearchQuery(query)
            }
    }

    LaunchedEffect(gridState) {
        snapshotFlow { gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .distinctUntilChanged()
            .filter { index ->
                index != null &&
                        index >= (uiState.searchResults.size - 3) &&
                        uiState.searchResults.isNotEmpty() &&
                        !uiState.isLoading &&
                        !uiState.isLoadingMore &&
                        uiState.hasMoreResults
            }
            .collect {
                viewModel.loadMore()
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            AppTextField(
                search,
                label = "",
                leadingIcon = Icons.Outlined.Search,
                placeholder = "Search for manga..."
            )
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            when {
                uiState.isLoading && uiState.searchResults.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                uiState.error != null && uiState.searchResults.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AppText(
                                text = "Error loading results",
                                variant = TextState.BodyLarge,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            AppText(
                                text = uiState.error ?: "Unknown error",
                                variant = TextState.BodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                uiState.searchResults.isEmpty() && uiState.currentQuery.isNotEmpty() && !uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Search,
                                contentDescription = "No results",
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            AppText(
                                text = "No results found",
                                variant = TextState.BodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            AppText(
                                text = "Try searching with different keywords",
                                variant = TextState.BodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                uiState.searchResults.isNotEmpty() -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        state = gridState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = uiState.searchResults,
                            key = { it.id }
                        ) { manga ->
                            SearchResultItem(
                                manga = manga,
                                onClick = { onMangaClick(manga.id) }
                            )
                        }

                        if (uiState.isLoadingMore) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                uiState.currentQuery.isEmpty() && uiState.searchResults.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Search,
                                contentDescription = "Search",
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            AppText(
                                text = "Search for manga",
                                variant = TextState.BodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            AppText(
                                text = "Type a title to start searching",
                                variant = TextState.BodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchResultItem(
    manga: SearchManga,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.7f),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onClick() }
        ){
            val imageUrl = manga.coverFileName?.let { fileName ->
                "https://uploads.mangadex.org/covers/${manga.id}/$fileName"
            }

            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .size(Size.ORIGINAL)
                    .crossfade(true)
                    .build()
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ){
                Image(
                    painter = painter,
                    contentDescription = manga.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.7f)
                                ),
                                startY = 0.6f
                            )
                        )
                )

                AppText(
                    text = manga.title,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp),
                    color = Color.White,
                    maxLines = 2,
                    variant = TextState.HeadingSmall
                )
            }

        }
    }
}