package com.chaos.mangaties.presentation.auth.forgetpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chaos.mangaties.domain.repository.auth.forgetpassword.ForgetPasswordRepository
import com.chaos.mangaties.domain.usecase.auth.forgetpassword.ForgotPasswordResult
import com.chaos.mangaties.domain.usecase.auth.forgetpassword.ForgotPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ForgotPasswordUiState(
    val isLoading: Boolean = false,
)

sealed class ForgotPasswordEvent{
    data class ShowToast(val message: String, val isError: Boolean = true) : ForgotPasswordEvent()
}

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val forgotPasswordUseCase: ForgotPasswordUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<ForgotPasswordEvent>(extraBufferCapacity = 1)
    val events : SharedFlow<ForgotPasswordEvent> = _event.asSharedFlow()

    fun sendPasswordResetEmail(email: String){
        viewModelScope.launch {
            forgotPasswordUseCase(email).collect { result ->
                when(result){
                    is ForgotPasswordResult.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }
                    is ForgotPasswordResult.Success ->{
                        _uiState.value = _uiState.value.copy(isLoading = false)
                        _event.emit(ForgotPasswordEvent.ShowToast(
                            message = "Rest Password Sent",
                            isError = false
                        ))
                    }
                    is ForgotPasswordResult.Error ->{
                        _uiState.value = _uiState.value.copy(isLoading = false)
                        _event.emit(ForgotPasswordEvent.ShowToast(
                            message = result.message,
                            isError = true
                        ))
                    }
                    is ForgotPasswordResult.ValidationError ->{
                        _uiState.value = _uiState.value.copy(isLoading = false)
                        _event.emit(ForgotPasswordEvent.ShowToast(
                            message = result.message,
                        ))
                    }
                }
            }
        }
    }

    fun resetState(){
        _uiState.value = ForgotPasswordUiState()
    }

}