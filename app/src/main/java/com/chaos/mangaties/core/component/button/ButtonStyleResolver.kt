package com.chaos.mangaties.core.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
internal fun resolveColors(
    type: ButtonType,
    enabled: Boolean
): ButtonColors = when {
    !enabled -> ButtonDefaults.buttonColors(
        disabledContainerColor = ColorDisabledBg,
        disabledContentColor = ColorDisabledText,
        containerColor = ColorDisabledBg,
        contentColor = ColorDisabledText,
    )
    type is ButtonType.Primary -> ButtonDefaults.buttonColors(
        containerColor = ColorBrand,
        contentColor = ColorTextOnPrimary,
    )
    type is ButtonType.Secondary -> ButtonDefaults.outlinedButtonColors(
        containerColor = Color.Transparent,
        contentColor = ColorTextBrand,
    )
    else -> ButtonDefaults.textButtonColors(
        containerColor = Color.Transparent,
        contentColor = ColorTextTertiary,
    )
}

internal fun resolveBorder(
    type: ButtonType,
    enabled: Boolean
): BorderStroke? = when {
    !enabled && type is ButtonType.Secondary -> BorderStroke(1.dp, ColorDisabledBorder)
    type is ButtonType.Secondary             -> BorderStroke(1.5.dp, ColorBrand)
    else                                     -> null
}