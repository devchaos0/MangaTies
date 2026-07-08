package com.chaos.mangaties.domain.usecase.auth.login

import com.chaos.mangaties.domain.model.auth.login.LoginValidationResult
import javax.inject.Inject

class LoginValidatePasswordUseCase @Inject constructor() {
    operator fun invoke (password: String) : LoginValidationResult{
        if (password.isEmpty()){
            return LoginValidationResult.Error("Password cannot be empty")
        }

        if (password.length < 8){
            return LoginValidationResult.Error("Password must be at least 8 characters")
        }

        return LoginValidationResult.Success
    }
}