package com.chaos.mangaties.domain.usecase.auth.login

import com.chaos.mangaties.domain.model.auth.login.LoginValidationResult
import com.chaos.mangaties.domain.repository.auth.login.LoginRepository
import com.chaos.mangaties.domain.usecase.auth.signup.SignUpResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.Dispatcher
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: LoginRepository,
    private val validateForm: ValidateLoginFormUseCase
){
    operator fun invoke(
        email: String,
        password: String
    ): Flow<LoginResult> = flow {
        val validationResult = validateForm(email, password)

        if (validationResult is LoginValidationResult.Error){
            emit(LoginResult.ValidationError(validationResult.message))
            return@flow
        }

        emit(LoginResult.Loading)

        try {
            val result = authRepository.login(email, password)
            emit(LoginResult.Success(result))
        }catch (e: Exception){
            emit(LoginResult.Error(e.localizedMessage ?: e.message ?: "Login Failed"))
        }
    }.flowOn(Dispatchers.IO)

}

sealed class LoginResult{
    object Loading: LoginResult()
    data class Success(val user: Any): LoginResult()
    data class Error(val message: String): LoginResult()
    data class ValidationError(val message: String) : LoginResult()
}