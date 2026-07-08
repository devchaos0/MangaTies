package com.chaos.mangaties.domain.usecase.auth.signup

import com.chaos.mangaties.domain.model.auth.signup.SignUpValidationResult
import com.chaos.mangaties.domain.repository.auth.signup.SignUpRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authRepository: SignUpRepository,
    private val validateForm: ValidateSignUpFormUseCase
) {
    operator fun invoke(
        email: String,
        password: String,
        confirmPassword: String,
    ): Flow<SignUpResult> = flow{
        val validationResult = validateForm(email, password,confirmPassword)

        if (validationResult is SignUpValidationResult.Error){
            emit(SignUpResult.ValidationError(validationResult.message))
            return@flow
        }

        emit(SignUpResult.Loading)

        try {
            val result = authRepository.signUp(email,password)
            emit(SignUpResult.Success(result))
        } catch (e: Exception){
            emit(SignUpResult.Error(e.localizedMessage ?: e.message ?: "Sign up failed"))
        }
    }.flowOn(Dispatchers.IO)
}

sealed class SignUpResult{
    object Loading: SignUpResult()
    data class Success(val user: Any) : SignUpResult()
    data class Error(val message: String) : SignUpResult()
    data class ValidationError(val message: String) : SignUpResult()

}