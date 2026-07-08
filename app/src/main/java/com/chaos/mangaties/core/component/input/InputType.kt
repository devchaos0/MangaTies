package com.chaos.mangaties.core.component.input

sealed class InputType {
    object Text : InputType()
    object Email : InputType()
    object Password : InputType()
}