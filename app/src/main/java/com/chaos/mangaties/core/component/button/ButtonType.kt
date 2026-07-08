package com.chaos.mangaties.core.component.button

sealed class ButtonType {
    object Primary : ButtonType()
    object Secondary : ButtonType()
    object Tertiary : ButtonType()
}