package com.chaos.mangaties.core.component.text

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp

@Composable
internal fun resolveStyle(variant: TextState): TextStyle = when (variant) {
    TextState.DisplayLarge  -> TextStyle(
        fontSize = 40.sp,
        fontWeight = FontWeight.ExtraBold,
        lineHeight = 48.sp,
        letterSpacing = (-0.5).sp
    )
    TextState.DisplaySmall  -> TextStyle(
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 40.sp,
        letterSpacing = (-0.3).sp
    )
    TextState.HeadingLarge  -> TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 32.sp
    )
    TextState.HeadingMedium -> TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 28.sp
    )
    TextState.HeadingSmall  -> TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 24.sp
    )
    TextState.BodyLarge     -> TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 26.sp
    )
    TextState.BodyMedium    -> TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 22.sp
    )
    TextState.BodySmall     -> TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 18.sp
    )
    TextState.Label         -> TextStyle(
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 18.sp
    )
    TextState.Overline      -> TextStyle(
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 16.sp,
        letterSpacing = 1.2.sp
    )
    TextState.Link          -> TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 22.sp,
        textDecoration = TextDecoration.Underline
    )
    TextState.Code          -> TextStyle(
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 20.sp,
        fontFamily = FontFamily.Monospace
    )
}