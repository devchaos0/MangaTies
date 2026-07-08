package com.chaos.mangaties.domain.usecase.auth.login

import android.util.Patterns
import com.chaos.mangaties.domain.model.auth.login.LoginValidationResult
import javax.inject.Inject

class LoginValidateEmailUseCase @Inject constructor() {
    operator fun invoke(email: String) : LoginValidationResult{
        if (email.isBlank()){
            return LoginValidationResult.Error("Email cannot be empty")
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()){
            return LoginValidationResult.Error("Please enter a valid email address")
        }

        return LoginValidationResult.Success
    }
}