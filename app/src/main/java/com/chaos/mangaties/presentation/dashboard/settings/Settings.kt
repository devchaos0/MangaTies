package com.chaos.mangaties.presentation.dashboard.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chaos.mangaties.core.component.text.*
import com.chaos.mangaties.presentation.dashboard.settings.component.*
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onMessageClick: () -> Unit = {},
    onHelpClick: () -> Unit = {},
    onAboutClick: () -> Unit = {},
    onPrivacyPolicyClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val email = currentUser?.email ?: "johndoe@gmail.com"

    val initials = email.split("@")
        .firstOrNull()
        ?.take(2)
        ?.uppercase() ?: "JD"


        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(vertical = 8.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {

            UserProfileSection(
                email = email,
                initials = initials
            )

            Spacer(modifier = Modifier.height(24.dp))

            SettingsOptions(
                onMessageClick = onMessageClick,
                onHelpClick = onHelpClick,
                onAboutClick = onAboutClick,
                onPrivacyPolicyClick = onPrivacyPolicyClick,
                onLogoutClick = onLogoutClick
            )

        }
    }