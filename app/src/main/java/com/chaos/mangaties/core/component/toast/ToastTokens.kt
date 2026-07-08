package com.chaos.mangaties.core.component.toast

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

internal data class ToastTokens(
    val bg: Color,
    val border: Color,
    val iconColor: Color,
    val titleColor: Color,
    val textColor: Color,
    val icon: ImageVector,
)

internal fun resolveTokens(type: ToastType) = when (type) {
    ToastType.Success -> ToastTokens(
        SuccessBg, SuccessBorder, SuccessIcon, SuccessTitle, SuccessText,
        Icons.Default.CheckCircle
    )
    ToastType.Error   -> ToastTokens(
        ErrorBg, ErrorBorder, ErrorIcon, ErrorTitle, ErrorText,
        Icons.Default.Cancel
    )
    ToastType.Info    -> ToastTokens(
        InfoBg, InfoBorder, InfoIcon, InfoTitle, InfoText,
        Icons.Default.Info
    )
}