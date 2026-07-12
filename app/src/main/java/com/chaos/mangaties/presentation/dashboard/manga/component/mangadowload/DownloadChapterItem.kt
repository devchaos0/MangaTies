package com.chaos.mangaties.presentation.dashboard.manga.component.mangadowload

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import com.chaos.mangaties.core.component.text.*
import com.chaos.mangaties.domain.model.manga.manga.MangaChapter
import kotlin.math.sin

@Composable
fun DownloadChapterItem(
    chapter: MangaChapter,
    isSelected: Boolean,
    isDownloaded: Boolean,
    isDownloading: Boolean,
    progress: Float,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !isDownloaded) { onToggle() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
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

                if (!isDownloaded) {
                    Checkbox(
                        checked = isSelected,
                        onCheckedChange = { onToggle() },
                        enabled = !isDownloading
                    )
                }
            }

            if (isDownloading) {
                WaveProgressIndicator(progress = progress)
            }
        }
    }
}

@Composable
fun WaveProgressIndicator(progress: Float) {
    val infiniteTransition = rememberInfiniteTransition(label = "wave")
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "waveOffset"
    )

    val color = MaterialTheme.colorScheme.primary

    Canvas(modifier = Modifier.fillMaxWidth().height(8.dp).padding(horizontal = 16.dp).padding(bottom = 8.dp).clip(MaterialTheme.shapes.small)) {
        val width = size.width
        val height = size.height

        // Draw background
        drawRect(color.copy(alpha = 0.2f))

        // Draw wave
        val path = Path()
        val waveAmplitude = 4f
        val waveFrequency = 2f
        
        path.moveTo(0f, height)
        for (x in 0..width.toInt()) {
            val y = sin(x.toFloat() / width * 2 * Math.PI * waveFrequency + waveOffset).toFloat() * waveAmplitude + (height * (1 - progress))
            path.lineTo(x.toFloat(), y)
        }
        path.lineTo(width, height)
        path.lineTo(0f, height)
        path.close()

        drawPath(path, color)
    }
}