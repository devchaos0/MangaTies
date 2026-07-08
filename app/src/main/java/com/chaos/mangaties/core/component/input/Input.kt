package com.chaos.mangaties.core.component.input

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chaos.mangaties.core.theme.ColorTextDisabled

@Composable
fun AppTextField(
    state: TextFieldState,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    inputType: InputType = InputType.Text,
    fieldState: InputState = InputState.Idle,
    leadingIcon: ImageVector? = null,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = 1,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    var passwordVisible by remember { mutableStateOf(false) }

    val borderColor = when {
        fieldState is InputState.Error   -> ColorBorderError
        fieldState is InputState.Success -> ColorBorderSuccess
        isFocused                        -> ColorBorderFocused
        else                             -> ColorBorderIdle
    }

    val labelColor = when {
        fieldState is InputState.Error   -> ColorLabelError
        fieldState is InputState.Success -> ColorLabelSuccess
        isFocused                        -> ColorBorderFocused
        else                             -> ColorLabelIdle
    }

    val keyboardType = when (inputType) {
        InputType.Email -> KeyboardType.Email
        InputType.Password -> KeyboardType.Password
        else -> KeyboardType.Text
    }

    val visualTransformation = if (inputType is InputType.Password && !passwordVisible) {
        PasswordVisualTransformation()
    } else {
        VisualTransformation.None
    }

    Column(modifier = modifier) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = labelColor,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        OutlinedTextField(
            value = state.text.toString(),
            onValueChange = { state.edit { replace(0, length, it) } },
            singleLine = singleLine,
            maxLines = maxLines,
            placeholder = {
                Text(
                    text = placeholder,
                    color = ColorHint,
                    fontSize = 14.sp
                )
            },
            leadingIcon = leadingIcon?.let { icon ->
                {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (isFocused) ColorIconActive else ColorIcon,
                        modifier = Modifier.size(18.dp)
                    )
                }
            },
            trailingIcon = {
                TrailingIcon(
                    inputType = inputType,
                    fieldState = fieldState,
                    passwordVisible = passwordVisible,
                    onTogglePassword = { passwordVisible = !passwordVisible }
                )
            },
            visualTransformation = visualTransformation,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            keyboardActions = keyboardActions,
            interactionSource = interactionSource,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = borderColor,
                unfocusedBorderColor = borderColor,
                disabledBorderColor = borderColor,
                errorBorderColor = borderColor,
                disabledTextColor = ColorTextDisabled,
                disabledPlaceholderColor = ColorHint,
            ),
            modifier = Modifier.width(342.dp),
            isError = fieldState is InputState.Error
        )

        AnimatedVisibility(
            visible = fieldState is InputState.Error || fieldState is InputState.Success,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            val (message, color) = when (fieldState) {
                is InputState.Error   -> fieldState.message to ColorLabelError
                is InputState.Success -> fieldState.message to ColorLabelSuccess
                else                  -> "" to Color.Transparent
            }

            if (message.isNotBlank()) {
                Text(
                    text = message,
                    fontSize = 12.sp,
                    color = color,
                    modifier = Modifier
                        .padding(top = 4.dp, start = 4.dp)
                )
            }
        }
    }
}