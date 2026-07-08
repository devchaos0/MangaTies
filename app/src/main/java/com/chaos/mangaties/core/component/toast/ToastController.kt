package com.chaos.mangaties.core.component.toast

import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf

class ToastController {
    var current by mutableStateOf<ToastData?>(null)
        private set

    fun show(
        message: String,
        type: ToastType,
        title: String? = null,
        duration: ToastDuration = ToastDuration.Short,
    ) {
        current = ToastData(message, type, title, duration)
    }

    fun dismiss() {
        current = null
    }
}

@Composable
fun rememberToastController(): ToastController {
    return remember { ToastController() }
}