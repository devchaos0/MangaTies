package com.chaos.mangaties.presentation.dashboard.manga.component.mangadowload

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.chaos.mangaties.core.component.text.*
import com.chaos.mangaties.domain.model.manga.manga.MangaChapter

@Composable
fun DownloadChapterItem(
    chapter: MangaChapter,
    isSelected: Boolean,
    isDownloaded: Boolean,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column{
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AppText(
                        text = "Chapter ${chapter.chapter}",
                        variant = TextState.BodyLarge,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                    if (isDownloaded) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "Downloaded",
                            tint = Color.Green,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                chapter.title?.let {
                    AppText(
                        text = it,
                        variant = TextState.BodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Checkbox(
                checked = isSelected,
                onCheckedChange = { onToggle() },
                enabled = !isDownloaded
            )
        }
    }
}