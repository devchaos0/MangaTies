package com.chaos.mangaties.core.component.toast

sealed class ToastType {
    object Success : ToastType()
    object Error   : ToastType()
    object Info    : ToastType()
}