package com.chaos.mangaties.presentation.dashboard.settings.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.chaos.mangaties.core.component.text.*

@Composable
fun SettingsItemRow(
    item: SettingsItem,
    isLast: Boolean
) {
    Column{
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { item.onClick() }
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                tint = if (item.isDestructive){
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.primary
                },
                modifier = Modifier.width(16.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                AppText(
                    text = item.title,
                    variant = TextState.BodyLarge,
                    color = if (item.isDestructive) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                )

                AppText(
                    text = item.description,
                    variant = TextState.BodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (!item.isDestructive){
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Navigate",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(24.dp)
                )
            }

        }

        if (!isLast){
            HorizontalDivider(
                modifier = Modifier.padding(start = 56.dp),
                thickness = DividerDefaults.Thickness,
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }
    }
}

data class SettingsItem(
    val id: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val isDestructive: Boolean = false,
    val onClick: () -> Unit
)