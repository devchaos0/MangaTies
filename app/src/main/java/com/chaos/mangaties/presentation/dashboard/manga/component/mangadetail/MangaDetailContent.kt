package com.chaos.mangaties.presentation.dashboard.manga.component.mangadetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.chaos.mangaties.core.component.text.*
import com.chaos.mangaties.domain.model.manga.manga.MangaChapter
import com.chaos.mangaties.domain.model.manga.manga.MangaDetails


enum class MangaTab {
    DETAILS,
    CHAPTERS
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MangaDetailContent(
    mangaDetail: MangaDetails?,
    mangaChapter: List<MangaChapter>,
    onChapterClick: (MangaChapter) -> Unit,
    onReadFirstChapter: () -> Unit,
    onLoadMoreChapters: () -> Unit,
    onDownloadClick: () -> Unit,
) {

    var selectedTab by remember {
        mutableStateOf(MangaTab.DETAILS)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        val imageUrl = mangaDetail?.coverFileName?.let { fileName ->
            "https://uploads.mangadex.org/covers/${mangaDetail.id}/$fileName"
        }

        if (imageUrl != null){
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .size(Size.ORIGINAL)
                    .crossfade(true)
                    .build()
            )

            Image(
                painter = painter,
                contentDescription = mangaDetail.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(bottomEnd = 16.dp, bottomStart = 16.dp)),
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .weight(1f)
        ) {
            AppText(
                text = mangaDetail?.title ?: "",
                color = MaterialTheme.colorScheme.primary,
                variant = TextState.HeadingMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            TabRow(
                selectedTabIndex = selectedTab.ordinal
            ) {
                Tab(
                    selected = selectedTab == MangaTab.DETAILS,
                    onClick = { selectedTab = MangaTab.DETAILS },
                    text = {
                        AppText(
                        text = "Details",
                        color = MaterialTheme.colorScheme.primary,
                    ) }
                )

                Tab(
                    selected = selectedTab == MangaTab.CHAPTERS,
                    onClick = { selectedTab = MangaTab.CHAPTERS },
                    text = {
                        AppText(
                            text = "Chapters (${mangaChapter.size})",
                             color = MaterialTheme.colorScheme.primary)
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.fillMaxSize().weight(1f)) {
                when (selectedTab) {
                    MangaTab.DETAILS -> {
                        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                            MangaDetailsTab(mangaDetail)
                        }
                    }
                    MangaTab.CHAPTERS -> {
                        MangaChaptersTab(
                            chapters = mangaChapter,
                            mangaId = mangaDetail?.id ?: "",
                            onLoadMore = onLoadMoreChapters,
                            onChapterClick = onChapterClick,
                            onReadFirstChapter = onReadFirstChapter,
                            onDownloadClick = onDownloadClick
                        )
                    }
                }
            }
        }
    }
}