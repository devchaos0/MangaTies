package com.chaos.mangaties.domain.usecase.auth


import android.util.Patterns
import com.chaos.mangaties.domain.model.auth.ValidationResult
import javax.inject.Inject

class ValidateEmailUseCase @Inject constructor() {
    operator fun invoke(email: String) : ValidationResult{
        if (email.isBlank()){
            return ValidationResult.Error("Email cannot be empty")
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()){
            return ValidationResult.Error("Please enter a valid email address")
        }

        return ValidationResult.Success
    }
}