package com.chaos.mangaties.presentation.auth.forgetpassword

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.chaos.mangaties.core.component.text.*
import com.chaos.mangaties.R
import com.chaos.mangaties.core.component.button.AppButton
import com.chaos.mangaties.core.component.button.ButtonType
import com.chaos.mangaties.core.component.input.AppTextField
import com.chaos.mangaties.core.component.toast.LocalToastController
import com.chaos.mangaties.core.component.toast.ToastType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    onBack: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: ForgotPasswordViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()
    val emailState = rememberTextFieldState()
    val context = LocalContext.current
    val toastController = LocalToastController.current

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess){
            toastController.show(
                message = "Password reset email sent successfully",
                type = ToastType.Success,
                title = "Success"
            )
            onSuccess()
        }
    }

    LaunchedEffect(uiState.error) {
        if (uiState.error != null){
            toastController.show(
                message = uiState.error ?: "An error occurred",
                type = ToastType.Error,
                title = "Error"
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    AppText(
                        text = "Forgot Password",
                        variant = TextState.HeadingMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .imePadding()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Image(
                    painter = painterResource(id = R.drawable.ic_forgot_password),contentDescription = "Forgot Password",
                    modifier = Modifier
                        .size(180.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(24.dp))

                AppText(
                    text = "Reset Your Password",
                    variant = TextState.HeadingLarge,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Description
                AppText(
                    text = "Enter the email address associated with your account and we'll send you a link to reset your password.",
                    variant = TextState.BodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Email Input
                AppTextField(
                    state = emailState,
                    label = "Email Address",
                    placeholder = "Enter your email",
                    leadingIcon = Icons.Default.Email,
                )

                if (uiState.error != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    AppText(
                        text = uiState.error ?: "",
                        variant = TextState.Label,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                AppButton(
                    text = if (uiState.isLoading) "Sending..." else "Reset Password",
                    onClick = {
                        val email = emailState.text.toString().trim()
                        if (email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            viewModel.sendPasswordResetEmail(email)
                        } else {
                            viewModel.setError("Please enter a valid email address")
                        }
                    },
                    type = ButtonType.Primary,
                    enabled = !uiState.isLoading,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AppText(
                        text = "Remember your password? ",
                        variant = TextState.BodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    AppText(
                        text = "Sign In",
                        variant = TextState.BodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { onBack() }
                    )
                }
            }
        }
    }

}
