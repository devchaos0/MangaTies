package com.chaos.mangaties.navgraph

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chaos.mangaties.domain.repository.statemanager.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthState(
    val isAuthenticated: Boolean = false,
    val isLoading: Boolean = true,
    val error: String? = null,
    val user: User? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authStateManager: AuthStateManager
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState(isLoading = true))
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        checkAuthState()
    }

    private fun checkAuthState() {
        viewModelScope.launch {
            authStateManager.isUserLoggedIn().collect { isLoggedIn ->
                val user = if (isLoggedIn) {
                    authStateManager.getCurrentUser()
                } else {
                    null
                }

                _authState.value = AuthState(
                    isAuthenticated = isLoggedIn,
                    isLoading = false,
                    user = user
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true)
            val result = authStateManager.logout()

            result.fold(
                onSuccess = {
                    _authState.value = AuthState(
                        isAuthenticated = false,
                        isLoading = false,
                        user = null
                    )
                },
                onFailure = { error ->
                    _authState.value = _authState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Failed to logout"
                    )
                }
            )
        }
    }
}