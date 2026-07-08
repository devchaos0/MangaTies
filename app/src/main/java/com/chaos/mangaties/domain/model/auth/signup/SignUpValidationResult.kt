package com.chaos.mangaties.domain.model.auth.signup

sealed class SignUpValidationResult {
    object Success : SignUpValidationResult()
    data class Error(val message: String) : SignUpValidationResult()
}