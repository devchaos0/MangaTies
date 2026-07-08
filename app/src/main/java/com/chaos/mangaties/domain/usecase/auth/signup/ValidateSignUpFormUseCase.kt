package com.chaos.mangaties.domain.usecase.auth.signup

import com.chaos.mangaties.domain.model.auth.signup.SignUpValidationResult
import javax.inject.Inject

class ValidateSignUpFormUseCase @Inject constructor(
    private val validateEmail: SignUpValidateEmailUseCase,
    private val validatePassword: SignUpValidatePasswordUseCase,
    private val validateConfirmPassword: SignUpValidateConfirmPasswordUseCase
) {
    operator fun invoke(
        email: String,
        password:String,
        confirmPassword:String
    ) : SignUpValidationResult{
        val emailValidation = validateEmail(email)
        if (emailValidation is SignUpValidationResult.Error) return emailValidation

        val passwordValidation = validatePassword(password)
        if (passwordValidation is SignUpValidationResult.Error) return passwordValidation

        val confirmPasswordValidation = validateConfirmPassword(password, confirmPassword)
        if (confirmPasswordValidation is SignUpValidationResult.Error) return confirmPasswordValidation

        return SignUpValidationResult.Success
    }
}