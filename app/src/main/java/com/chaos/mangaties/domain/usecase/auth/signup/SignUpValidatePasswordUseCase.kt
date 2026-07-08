package com.chaos.mangaties.domain.usecase.auth.signup

import com.chaos.mangaties.domain.model.auth.signup.SignUpValidationResult
import javax.inject.Inject

class SignUpValidatePasswordUseCase @Inject constructor() {
    operator fun invoke(password: String) : SignUpValidationResult {
        if (password.isEmpty()){
            return SignUpValidationResult.Error("Password cannot be empty")
        }

        if (password.length < 8){
            return SignUpValidationResult.Error("Password must be at least 8 characters")
        }

        return SignUpValidationResult.Success
    }
}