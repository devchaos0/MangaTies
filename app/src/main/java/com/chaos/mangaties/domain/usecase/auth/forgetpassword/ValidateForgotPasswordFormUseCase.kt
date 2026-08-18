package com.chaos.mangaties.domain.usecase.auth.forgetpassword

import com.chaos.mangaties.domain.model.auth.ValidationResult
import com.chaos.mangaties.domain.usecase.auth.ValidateEmailUseCase
import javax.inject.Inject


class ValidateForgotPasswordFormUseCase @Inject constructor(
    private val validateEmailUseCase: ValidateEmailUseCase,
){
    operator fun invoke(
        email: String,
    ) : ValidationResult{

        val emailValidation = validateEmailUseCase(email)
        if (emailValidation is ValidationResult.Error) return emailValidation

        return ValidationResult.Success

    }
}