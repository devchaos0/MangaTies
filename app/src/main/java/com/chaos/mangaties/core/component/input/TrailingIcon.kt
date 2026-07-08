package com.chaos.mangaties.core.component.input

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun TrailingIcon(
    inputType: InputType,
    fieldState: InputState,
    passwordVisible: Boolean,
    onTogglePassword: () -> Unit,
) {
    when {
        inputType is InputType.Password -> {
            IconButton(onClick = onTogglePassword) {
                Icon(
                    imageVector = if (passwordVisible)
                        Icons.Outlined.VisibilityOff
                    else
                        Icons.Outlined.Visibility,
                    contentDescription = if (passwordVisible)
                        "Hide password"
                    else
                        "Show password",
                    tint = ColorIcon,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        fieldState is InputState.Error -> {
            Icon(
                imageVector = Icons.Outlined.ErrorOutline,
                contentDescription = "Error",
                tint = ColorLabelError,
                modifier = Modifier.size(18.dp)
            )
        }
        fieldState is InputState.Success -> {
            Icon(
                imageVector = Icons.Outlined.CheckCircleOutline,
                contentDescription = "Success",
                tint = ColorLabelSuccess,
                modifier = Modifier.size(18.dp)
            )
        }
        else -> {}
    }
}