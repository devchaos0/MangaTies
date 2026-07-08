package com.chaos.mangaties.core.component.button

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

internal data class SizeTokens(
    val height: Dp,
    val horizontalPadding: Dp,
    val fontSize: TextUnit,
    val iconSize: Dp,
)

internal fun resolveSize(size: ButtonSize): SizeTokens = when (size) {
    ButtonSize.Small  -> SizeTokens(
        height = 36.dp,
        horizontalPadding = 16.dp,
        fontSize = 13.sp,
        iconSize = 14.dp
    )
    ButtonSize.Medium -> SizeTokens(
        height = 48.dp,
        horizontalPadding = 24.dp,
        fontSize = 15.sp,
        iconSize = 16.dp
    )
    ButtonSize.Large  -> SizeTokens(
        height = 56.dp,
        horizontalPadding = 142.dp,
        fontSize = 16.sp,
        iconSize = 18.dp
    )
}