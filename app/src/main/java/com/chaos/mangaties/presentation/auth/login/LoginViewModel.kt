package com.chaos.mangaties.presentation.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chaos.mangaties.domain.usecase.auth.login.LoginResult
import com.chaos.mangaties.domain.usecase.auth.login.LoginUseCase
import com.chaos.mangaties.domain.usecase.auth.signup.SignUpResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val isLoading: Boolean = false
)

sealed class LoginEvent{
    data class ShowToast(val message: String, val isError: Boolean = true) : LoginEvent()
    object NavigateToDashboard : LoginEvent()

}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
)  : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState : StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<LoginEvent>(extraBufferCapacity = 1)
    val events : SharedFlow<LoginEvent> = _event.asSharedFlow()

    fun login(email: String, password: String){
        viewModelScope.launch {
            loginUseCase(email, password).collect { result ->
                when(result){
                    is LoginResult.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }
                    is LoginResult.Success -> {
                        _uiState.value = _uiState.value.copy(isLoading = false)
                        _event.emit(LoginEvent.ShowToast(
                            message = "Welcome back",
                            isError = false
                        ))
//                        delay(1500L)
                        _event.emit(LoginEvent.NavigateToDashboard)
                    }
                    is LoginResult.Error -> {
                        _uiState.value = _uiState.value.copy(isLoading = false)
                        _event.emit(LoginEvent.ShowToast(
                            message = result.message,
                            isError = true
                        ))
                    }

                    is LoginResult.ValidationError -> {
                        _uiState.value = _uiState.value.copy(isLoading = false)
                        _event.emit(LoginEvent.ShowToast(
                            message = result.message,
                        ))
                    }
                }
            }
        }
    }


}