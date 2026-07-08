package com.chaos.mangaties.core.component.text

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.chaos.mangaties.core.theme.*

@Composable
internal fun resolveColor(variant: TextState): Color = when (variant) {
    TextState.DisplayLarge,
    TextState.DisplaySmall,
    TextState.HeadingLarge,
    TextState.HeadingMedium,
    TextState.HeadingSmall  -> ColorTextPrimary

    TextState.BodyLarge,
    TextState.BodyMedium    -> ColorTextPrimary

    TextState.BodySmall     -> ColorTextSecondary
    TextState.Label         -> ColorTextSecondary
    TextState.Overline      -> ColorTextMuted
    TextState.Link          -> ColorTextLink
    TextState.Code          -> ColorTextPrimary
}