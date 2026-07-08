package com.chaos.mangaties.presentation.dashboard.manga.component.mangadowload

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.chaos.mangaties.core.component.text.*

@Composable
fun DownloadProgressOverlay(
    progress: Float,
    currentChapter: String?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
        progress = { progress },
        modifier = Modifier.size(60.dp),
        color = Color.White,
        strokeWidth = ProgressIndicatorDefaults.CircularStrokeWidth,
        trackColor = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
        strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap,
        )
        Spacer(modifier = Modifier.height(16.dp))
        AppText(
            text = "Downloading ${(progress * 100).toInt()}%",
            color = Color.White,
            variant = TextState.BodyLarge
        )
        currentChapter?.let {
            AppText(
                text = "Chapter $it",
                color = Color.White.copy(alpha = 0.7f),
                variant = TextState.BodySmall
            )
        }
    }
}