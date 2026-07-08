package com.chaos.mangaties.presentation.dashboard.manga.component.mangadetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chaos.mangaties.core.component.text.*
import com.chaos.mangaties.domain.model.manga.manga.MangaDetails

@Composable
fun MangaDetailsTab(
    mangaDetail: MangaDetails?
) {

    DetailRow(
        label = "Status",
        value = mangaDetail?.status?.replaceFirstChar { it.uppercase() }
    )

    DetailRow(
        label = "Content Rating",
        value = mangaDetail?.contentRating?.replaceFirstChar { it.uppercase() }
    )

    mangaDetail?.publicationDemographic?.let { demographic ->
        DetailRow(
            label = "Demographic",
            value = demographic.replaceFirstChar { it.uppercase() }
        )
    }

    mangaDetail?.year?.let { year ->
        DetailRow(
            label = "Year",
            value = year.toString()
        )
    }

    DetailRow(
        label = "Language",
        value = when (mangaDetail?.originalLanguage) {
            "ja" -> "Japanese"
            "en" -> "English"
            "ko" -> "Korean"
            "zh" -> "Chinese"
            else -> mangaDetail?.originalLanguage
        }
    )

    if (mangaDetail?.tags?.isNotEmpty() == true) {

        Spacer(modifier = Modifier.height(22.dp))

        AppText(
            text = "Tags",
            color = MaterialTheme.colorScheme.onSurface,
            variant = TextState.BodyMedium
        )

        Spacer(modifier = Modifier.height(4.dp))

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 100.dp),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 200.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(mangaDetail.tags) { tag ->
                AssistChip(
                    onClick = {},
                    label = {
                        AppText(
                            text = tag.attributes.name["en"] ?: "",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        AppText(
            text = "Description",
            color = MaterialTheme.colorScheme.primary,
            variant = TextState.BodyMedium
        )

        Spacer(modifier = Modifier.height(4.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            AppText(
                text = mangaDetail.description ?: "No description available",
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun DetailRow(
    label: String,
    value: String?,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AppText(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        AppText(
            text = value ?: "",
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
