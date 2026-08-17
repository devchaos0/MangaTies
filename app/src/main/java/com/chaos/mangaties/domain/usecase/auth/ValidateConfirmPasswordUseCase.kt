package com.chaos.mangaties.domain.usecase.auth


import com.chaos.mangaties.domain.model.auth.ValidationResult
import javax.inject.Inject

class ValidateConfirmPasswordUseCase @Inject constructor() {
    operator fun invoke(password: String, confirmPassword: String): ValidationResult {
        if (confirmPassword.isEmpty()) {
            return ValidationResult.Error("Please confirm your password")
        }
        if (password != confirmPassword) {
            return ValidationResult.Error("Passwords do not match")
        }
        return ValidationResult.Success
    }
}