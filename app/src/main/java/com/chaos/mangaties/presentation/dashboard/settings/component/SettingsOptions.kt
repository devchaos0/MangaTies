package com.chaos.mangaties.presentation.dashboard.settings.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsOptions(
    onMessageClick: () -> Unit,
    onHelpClick: () -> Unit,
    onAboutClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val settingsItem = listOf(
        SettingsItem(
            id = "messages",
            title = "Messages",
            description = "View your messages and notifications",
            icon = Icons.AutoMirrored.Outlined.Message,
            onClick = onMessageClick
        ),
        SettingsItem(
            id = "help",
            title = "Help & Support",
            description = "Get help and contact support",
            icon = Icons.AutoMirrored.Outlined.Help,
            onClick = onHelpClick
        ),
        SettingsItem(
            id = "about",
            title = "About",
            description = "Version and app information",
            icon = Icons.Outlined.Info,
            onClick = onAboutClick
        ),
        SettingsItem(
            id = "privacy",
            title = "Privacy Policy",
            description = "Read our privacy policy",
            icon = Icons.Outlined.PrivacyTip,
            onClick = onPrivacyPolicyClick
        ),
        SettingsItem(
            id = "logout",
            title = "Logout",
            description = "Sign out of your account",
            icon = Icons.AutoMirrored.Outlined.Logout,
            isDestructive = true,
            onClick = onLogoutClick
        )
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            settingsItem.forEachIndexed { index, item ->
                SettingsItemRow(
                    item = item,
                    isLast = index == settingsItem.size - 1
                )
            }
        }
    }
}