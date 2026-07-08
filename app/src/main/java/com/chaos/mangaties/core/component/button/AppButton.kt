package com.chaos.mangaties.core.component.button

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chaos.mangaties.core.component.text.AppText
import com.chaos.mangaties.core.component.text.TextState

/**
 * Versatile button component that supports both simple text usage and custom content.
 *
 * Simple usage:
 * ```
 * AppButton(
 *     text = "Sign Up",
 *     onClick = { /* action */ },
 *     type = ButtonType.Primary
 * )
 * ```
 *
 * Custom content usage:
 * ```
 * AppButton(
 *     onClick = { /* action */ },
 *     type = ButtonType.Primary
 * ) {
 *     Icon(Icons.Default.Add, null)
 *     Text("Add Item")
 * }
 * ```
 *
 * Loading state:
 * ```
 * AppButton(
 *     text = "Submit",
 *     onClick = { /* action */ },
 *     isLoading = true
 * )
 * ```
 *
 * Custom loading:
 * ```
 * AppButton(
 *     onClick = { /* action */ },
 *     isLoading = true,
 *     loadingContent = {
 *         CircularProgressIndicator(...)
 *     }
 * ) {
 *     Text("Submit")
 * }
 * ```
 */
@Composable
fun AppButton(
    text: String? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    type: ButtonType = ButtonType.Primary,
    size: ButtonSize = ButtonSize.Medium,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    loadingContent: @Composable (() -> Unit)? = null,
    content: (@Composable RowScope.() -> Unit)? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Scale animation on press
    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) 0.97f else 1f,
        label = "button_scale"
    )

    // Resolve size tokens
    val (height, horizontalPadding, fontSize, iconSize) = resolveSize(size)

    // Resolve colors and border
    val colors = resolveColors(type, enabled)
    val border = resolveBorder(type, enabled)

    Button(
        onClick = { if (!isLoading) onClick() },
        enabled = enabled,
        interactionSource = interactionSource,
        shape = RoundedCornerShape(12.dp),
        colors = colors,
        border = border,
        contentPadding = PaddingValues(
            horizontal = horizontalPadding,
            vertical = 0.dp
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = if (type is ButtonType.Primary) 2.dp else 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp
        ),
        modifier = modifier
            .height(height)
            .scale(scale)
    ) {
        when {
            // Loading state
            isLoading -> {
                if (loadingContent != null) {
                    loadingContent()
                } else {
                    // Default loading indicator
                    CircularProgressIndicator(
                        modifier = Modifier.size(iconSize),
                        strokeWidth = 2.dp,
                        color = if (type is ButtonType.Primary)
                            ColorTextOnPrimary
                        else
                            ColorBrand
                    )
                }
            }

            // Custom content provided
            content != null -> {
                content()
            }

            // Text-based content (default)
            text != null -> {
                leadingIcon?.let { icon ->
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(iconSize)
                    )
                }

                AppText(
                    text = text,
                    color = MaterialTheme.colorScheme.primary,
                    variant = TextState.HeadingSmall
                )

                trailingIcon?.let { icon ->
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(iconSize)
                    )
                }
            }
        }
    }
}