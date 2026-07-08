package com.chaos.mangaties.presentation.dashboard.bookshelf.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.chaos.mangaties.core.component.text.*
import com.chaos.mangaties.domain.model.manga.manga.FavouriteManga
import com.chaos.mangaties.presentation.dashboard.bookshelf.FavouriteViewModel
import androidx.compose.foundation.lazy.grid.items
@Composable
fun Favourite(
    onFavouriteClick: (String) -> Unit,
    viewModel: FavouriteViewModel = hiltViewModel()
) {

    val favouriteState by viewModel.favouriteMangas.collectAsState()

    if (favouriteState.isLoading){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator()
        }
    } else if (favouriteState.mangas.isEmpty()){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "No favourites",
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                AppText(
                    text = "No favourite manga",
                    variant = TextState.BodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                AppText(
                    text = "Heart a manga to add it to favourites",
                    variant = TextState.BodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    } else{
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(favouriteState.mangas) { manga ->
                FavouriteMangaItem(
                    manga = manga,
                    onClick = { onFavouriteClick(manga.mangaId) }
                )
            }
        }
    }
}

    @Composable
    fun FavouriteMangaItem(
        manga: FavouriteManga,
        onClick: () -> Unit
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Cover Image
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
                            contentDescription = manga.title,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(48.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    // Heart icon overlay (bottom right)
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f),
                                shape = RoundedCornerShape(50)
                            )
                            .size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favourite",
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(16.dp),
                            tint = Color.Red
                        )
                    }
                }

                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    AppText(
                        text = manga.title,
                        variant = TextState.BodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }