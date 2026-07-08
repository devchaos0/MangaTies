package com.chaos.mangaties.presentation.auth.forgetpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chaos.mangaties.domain.repository.auth.forgetpassword.ForgetPasswordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ForgotPasswordUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authRepository: ForgetPasswordRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()

    fun sendPasswordResetEmail(email: String){
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    isSuccess = false
                )
            }

            val result = authRepository.sendPasswordResetEmail(email)

            result.fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true,
                            error = null
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = false,
                            error = error.message ?: "Failed to send reset email"
                        )
                    }
                }
            )

        }
    }

    fun resetState(){
        _uiState.value = ForgotPasswordUiState()
    }

    fun setError(message: String){
        _uiState.update {
            it.copy(error = message)
        }
    }

}