package com.chaos.mangaties.domain.usecase.auth.signup

import com.chaos.mangaties.domain.model.auth.signup.SignUpValidationResult
import javax.inject.Inject

class SignUpValidateConfirmPasswordUseCase @Inject constructor() {
    operator fun invoke(password: String, confirmPassword: String): SignUpValidationResult {
        if (confirmPassword.isEmpty()) {
            return SignUpValidationResult.Error("Please confirm your password")
        }
        if (password != confirmPassword) {
            return SignUpValidationResult.Error("Passwords do not match")
        }
        return SignUpValidationResult.Success
    }
}