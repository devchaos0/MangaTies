package com.chaos.mangaties.core.component.toast

sealed class ToastDuration(val ms: kotlin.Long) {
    object Short  : ToastDuration(2_500L)
    object Long   : ToastDuration(4_000L)
}