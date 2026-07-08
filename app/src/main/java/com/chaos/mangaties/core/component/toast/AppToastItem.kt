package com.chaos.mangaties.core.component.toast

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun AppToastItem(
    data: ToastData,
    onDismiss: () -> Unit,
) {
    val tokens = resolveTokens(data.type)

    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 24.dp)
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(14.dp),
                ambientColor = tokens.border
            )
            .clip(RoundedCornerShape(14.dp))
            .background(tokens.bg),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Left accent bar
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(64.dp)
                .background(tokens.border)
        )

        Spacer(Modifier.width(12.dp))

        // Icon
        Icon(
            imageVector = tokens.icon,
            contentDescription = null,
            tint = tokens.iconColor,
            modifier = Modifier.size(22.dp)
        )

        Spacer(Modifier.width(10.dp))

        // Text content
        Column(modifier = Modifier.weight(1f)) {
            data.title?.let {
                Text(
                    text = it,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = tokens.titleColor,
                )
            }
            Text(
                text = data.message,
                fontSize = 13.sp,
                fontWeight = if (data.title != null) FontWeight.Normal else FontWeight.Medium,
                color = tokens.textColor,
                lineHeight = 18.sp,
            )
        }

        // Dismiss button
        IconButton(onClick = onDismiss, modifier = Modifier.size(36.dp)) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Dismiss",
                tint = tokens.textColor.copy(alpha = 0.5f),
                modifier = Modifier.size(16.dp)
            )
        }

        Spacer(Modifier.width(4.dp))
    }
}