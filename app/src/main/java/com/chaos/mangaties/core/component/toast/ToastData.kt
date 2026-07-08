package com.chaos.mangaties.core.component.toast

data class ToastData(
    val message: String,
    val type: ToastType,
    val title: String? = null,
    val duration: ToastDuration = ToastDuration.Short,
)