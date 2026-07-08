package com.chaos.mangaties.core.component.input

sealed class InputState {
    object Idle : InputState()
    data class Error(val message: String): InputState()
    data class Success(val message: String = "") : InputState()
}
