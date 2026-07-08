package com.chaos.mangaties.domain.usecase.auth.signup

import android.util.Patterns
import com.chaos.mangaties.domain.model.auth.signup.SignUpValidationResult
import javax.inject.Inject

class SignUpValidateEmailUseCase @Inject constructor(){
    operator fun invoke(email: String) : SignUpValidationResult {
        if (email.isBlank()){
            return SignUpValidationResult.Error("Email cannot be empty")
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()){
            return SignUpValidationResult.Error("Please enter a valid email address")
        }

        return SignUpValidationResult.Success
    }
}