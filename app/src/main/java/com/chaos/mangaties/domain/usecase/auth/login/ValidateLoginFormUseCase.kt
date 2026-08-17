package com.chaos.mangaties.domain.usecase.auth.login

import com.chaos.mangaties.domain.model.auth.ValidationResult
import com.chaos.mangaties.domain.usecase.auth.ValidateEmailUseCase
import com.chaos.mangaties.domain.usecase.auth.ValidatePasswordUseCase
import javax.inject.Inject

class ValidateLoginFormUseCase @Inject constructor(
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase
){
    operator fun invoke(
        email: String,
        password: String
    ) : ValidationResult{

        val emailValidation = validateEmailUseCase(email)
        if (emailValidation is ValidationResult.Error) return emailValidation

        val passwordValidation = validatePasswordUseCase(password)
        if (passwordValidation is ValidationResult.Error) return passwordValidation

        return ValidationResult.Success

    }
}