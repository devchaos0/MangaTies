package com.chaos.mangaties.presentation.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chaos.mangaties.domain.usecase.auth.signup.SignUpResult
import com.chaos.mangaties.domain.usecase.auth.signup.SignUpUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

data class SignUpUiState (
    val isLoading : Boolean = false
)

sealed class SignUpEvent {
    data class ShowToast(val message: String, val isError: Boolean = true) : SignUpEvent()
    object NavigateToLogin : SignUpEvent()
}

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<SignUpEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<SignUpEvent> = _events.asSharedFlow()

    fun signUp (email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            signUpUseCase(email, password, confirmPassword).collect{ result ->
                when(result){
                    is SignUpResult.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }
                    is SignUpResult.Success -> {
                        _uiState.value = _uiState.value.copy(isLoading = false)
                        _events.emit(SignUpEvent.ShowToast(
                            message = "Account created successfully! Please verify your email",
                            isError = false
                        ))
                        _events.emit(SignUpEvent.NavigateToLogin)
                    }
                    is SignUpResult.Error -> {
                        _uiState.value = _uiState.value.copy(isLoading = false)
                        _events.emit(
                            SignUpEvent.ShowToast(
                                message = result.message,
                                isError = true
                            )
                        )
                    }
                    is SignUpResult.ValidationError -> {
                        _uiState.value = _uiState.value.copy(isLoading = false)
                        _events.emit(
                            SignUpEvent.ShowToast(
                                message = result.message,
                            )
                        )
                    }
                }
            }
        }
    }


}