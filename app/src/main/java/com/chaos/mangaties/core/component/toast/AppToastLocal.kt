package com.chaos.mangaties.core.component.toast

import androidx.compose.runtime.compositionLocalOf

val LocalToastController = compositionLocalOf<ToastController> {
    error("No ToastController provided")
}