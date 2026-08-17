package com.chaos.mangaties.domain.usecase.auth.signup

import com.chaos.mangaties.domain.model.auth.ValidationResult
import com.chaos.mangaties.domain.model.auth.signup.SignUpValidationResult
import com.chaos.mangaties.domain.usecase.auth.ValidateConfirmPasswordUseCase
import com.chaos.mangaties.domain.usecase.auth.ValidateEmailUseCase
import com.chaos.mangaties.domain.usecase.auth.ValidatePasswordUseCase
import javax.inject.Inject

class ValidateSignUpFormUseCase @Inject constructor(
    private val validateEmail: ValidateEmailUseCase,
    private val validatePassword: ValidatePasswordUseCase,
    private val validateConfirmPassword: ValidateConfirmPasswordUseCase
) {
    operator fun invoke(
        email: String,
        password:String,
        confirmPassword:String
    ) : ValidationResult{
        val emailValidation = validateEmail(email)
        if (emailValidation is ValidationResult.Error) return emailValidation

        val passwordValidation = validatePassword(password)
        if (passwordValidation is ValidationResult.Error) return passwordValidation

        val confirmPasswordValidation = validateConfirmPassword(password, confirmPassword)
        if (confirmPasswordValidation is ValidationResult.Error) return confirmPasswordValidation

        return ValidationResult.Success
    }
}