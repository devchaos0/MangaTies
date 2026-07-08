package com.chaos.mangaties.domain.usecase.auth.login

import com.chaos.mangaties.domain.model.auth.login.LoginValidationResult
import javax.inject.Inject

class ValidateLoginFormUseCase @Inject constructor(
    private val validateEmailUseCase: LoginValidateEmailUseCase,
    private val validatePasswordUseCase: LoginValidatePasswordUseCase
){
    operator fun invoke(
        email: String,
        password: String
    ) : LoginValidationResult{

        val emailValidation = validateEmailUseCase(email)
        if (emailValidation is LoginValidationResult.Error) return emailValidation

        val passwordValidation = validatePasswordUseCase(password)
        if (passwordValidation is LoginValidationResult.Error) return passwordValidation

        return LoginValidationResult.Success

    }
}