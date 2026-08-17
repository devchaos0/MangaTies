package com.chaos.mangaties.domain.usecase.auth

import com.chaos.mangaties.domain.model.auth.ValidationResult
import javax.inject.Inject

class ValidatePasswordUseCase @Inject constructor() {
    operator fun invoke (password: String) : ValidationResult{
        if (password.isEmpty()){
            return ValidationResult.Error("Password cannot be empty")
        }

        if (password.length < 8){
            return ValidationResult.Error("Password must be at least 8 characters")
        }

        return ValidationResult.Success
    }
}