package com.chaos.mangaties.presentation.auth.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.chaos.mangaties.core.component.input.*
import com.chaos.mangaties.core.component.button.*
import com.chaos.mangaties.core.component.text.*
import com.chaos.mangaties.core.component.toast.LocalToastController
import com.chaos.mangaties.core.component.toast.ToastType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(
    navigateToSignUp: () -> Unit,
    navigateToDashboard: () -> Unit,
    navigateToForgotPassword: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val email = rememberTextFieldState()
    val password = rememberTextFieldState()

    val emailValue = email.text.toString()
    val passwordValue = password.text.toString()

    val uiState by viewModel.uiState.collectAsState()
    val toastController = LocalToastController.current


    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when(event) {
                is LoginEvent.ShowToast -> {
                    toastController.show(
                        message = event.message,
                        type = if (event.isError) ToastType.Error else ToastType.Success,
                        title = if (event.isError) "Error" else "Success"
                    )
                }

                is LoginEvent.NavigateToDashboard -> navigateToDashboard()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        AppText(
                            text = "Login",
                            variant = TextState.HeadingLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth().align(Alignment.Center),
                            color= MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column(
                verticalArrangement = Arrangement.spacedBy(22.dp)
            ) {
                AppTextField(
                    email,
                    label = "Email",
                    leadingIcon = Icons.Outlined.Email,
                    placeholder = "Enter your email"
                )
                AppTextField(
                    password,
                    label = "Password",
                    leadingIcon = Icons.Outlined.Lock,
                    placeholder = "Min 8 characters",
                    inputType = InputType.Password,
                )
            }

            Box(
                modifier = Modifier.align(alignment = Alignment.End)
            ) {
                AppButton(
                    "Forget password",
                    type = ButtonType.Tertiary,
                    onClick = navigateToForgotPassword
                )
            }

            Box {
                AppButton(
                    type = ButtonType.Primary,
                    onClick = { viewModel.login(emailValue, passwordValue) },
                    size = ButtonSize.Large,
                    enabled = !uiState.isLoading
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        AppText("Login")
                    }
                }
            }
            Spacer(modifier = Modifier.height(42.dp))
            Box() {
                AppButton(
                    "Create an account",
                    type = ButtonType.Tertiary,
                    onClick = navigateToSignUp
                )
            }
        }
    }
}