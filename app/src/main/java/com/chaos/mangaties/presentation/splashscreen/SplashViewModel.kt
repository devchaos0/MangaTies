package com.chaos.mangaties.presentation.splashscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chaos.mangaties.domain.repository.statemanager.AuthStateManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SplashUiState(
    val isLoading: Boolean = true,
    val isAuthenticated: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authStateManager: AuthStateManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    init {
        checkAuthState()
    }

    private fun checkAuthState() {
        viewModelScope.launch {
            authStateManager.isUserLoggedIn().collect { isLoggedIn ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isAuthenticated = isLoggedIn
                    )
                }
            }
        }
    }
}