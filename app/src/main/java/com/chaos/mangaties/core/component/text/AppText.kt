package com.chaos.mangaties.core.component.text

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun AppText(
    text: String?,
    modifier: Modifier = Modifier,
    variant: TextState = TextState.BodyMedium,
    color: Color = Color.Unspecified,
    textAlign: TextAlign = TextAlign.Start,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
) {
    val resolvedStyle = resolveStyle(variant)
    val resolvedColor = if (color != Color.Unspecified) color else resolveColor(variant)

    Text(
        text = text ?: "",
        modifier = modifier,
        style = resolvedStyle,
        color = resolvedColor,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow,
        softWrap = softWrap,
    )
}
