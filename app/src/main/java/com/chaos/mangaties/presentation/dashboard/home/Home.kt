package com.chaos.mangaties.presentation.dashboard.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.chaos.mangaties.presentation.dashboard.home.component.MangaGridItem
import kotlinx.coroutines.flow.*
import java.util.UUID.randomUUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onMangaClick: (String) -> Unit,
    viewModel: MangaHomeListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val gridState = rememberLazyGridState()

    LaunchedEffect(gridState) {
        snapshotFlow { gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .distinctUntilChanged()
            .filter { index ->
                index != null &&
                        index >= (uiState.mangaList.size - 3) &&
                        uiState.mangaList.isNotEmpty() &&
                        !uiState.isLoading
            }
            .collect {
                viewModel.loadMoreManga()
            }
    }

    uiState.error?.let { error ->
        LaunchedEffect(error) {

        }
    }

    Column() {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ){
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                state = gridState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = uiState.mangaList,
                    key = { manga -> manga.id ?: randomUUID().toString() },
                ) { manga ->
                    MangaGridItem(
                        manga = manga,
                        onClick = { manga.id?.let { onMangaClick(it) } }
                    )
                }

                if (uiState.isLoading){
                    item (span = { GridItemSpan(maxLineSpan) }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ){
                            CircularProgressIndicator(
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}