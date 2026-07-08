package com.chaos.mangaties.domain.model.auth.login

sealed class LoginValidationResult {
    object Success : LoginValidationResult()
    data class Error(val message: String) : LoginValidationResult()
}