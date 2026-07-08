package com.chaos.mangaties.core.component.toast


import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AppToastHost(controller: ToastController) {
    val scope = rememberCoroutineScope()

    LaunchedEffect(controller.current) {
        controller.current?.let { toast ->
            scope.launch {
                delay(toast.duration.ms)
                controller.dismiss()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(999f),
        contentAlignment = Alignment.BottomCenter
    ) {
        AnimatedVisibility(
            visible = controller.current != null,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            ) + fadeIn(),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(250)
            ) + fadeOut()
        ) {
            controller.current?.let { toast ->
                AppToastItem(
                    data = toast,
                    onDismiss = { controller.dismiss() }
                )
            }
        }
    }
}