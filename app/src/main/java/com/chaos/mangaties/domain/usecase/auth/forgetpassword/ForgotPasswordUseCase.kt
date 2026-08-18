package com.chaos.mangaties.domain.usecase.auth.forgetpassword

import com.chaos.mangaties.domain.model.auth.ValidationResult
import com.chaos.mangaties.domain.repository.auth.forgetpassword.ForgetPasswordRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ForgotPasswordUseCase @Inject constructor(
  private val authRepository: ForgetPasswordRepository,
    private val validateForm: ValidateForgotPasswordFormUseCase
)  {
    operator fun invoke(
        email: String
    ) : Flow<ForgotPasswordResult> = flow {
        val validationResult = validateForm(email)

        if (validationResult is ValidationResult.Error){
            emit(ForgotPasswordResult.ValidationError(validationResult.message))
            return@flow
        }

        emit(ForgotPasswordResult.Loading)

        try {
            val result = authRepository.sendPasswordResetEmail(email)
            if (result.isSuccess) {
                emit(ForgotPasswordResult.Success(result))
            } else {
                emit(ForgotPasswordResult.Error(result.exceptionOrNull()?.localizedMessage ?: "Forget password failed"))
            }
        } catch (e: Exception){
            emit(ForgotPasswordResult.Error(e.localizedMessage ?: e.message ?: "Forget password failed"))
        }
    }.flowOn(Dispatchers.IO)
}

sealed class ForgotPasswordResult{
    object Loading: ForgotPasswordResult()
    data class Success(val user: Any): ForgotPasswordResult()
    data class Error(val message: String): ForgotPasswordResult()
    data class ValidationError(val message: String) : ForgotPasswordResult()
}
