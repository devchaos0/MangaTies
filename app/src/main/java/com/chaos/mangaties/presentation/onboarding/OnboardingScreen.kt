package com.chaos.mangaties.presentation.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.chaos.mangaties.R
import com.chaos.mangaties.core.component.button.AppButton
import com.chaos.mangaties.core.component.button.ButtonSize
import com.chaos.mangaties.core.component.button.ButtonType
import com.chaos.mangaties.core.component.text.AppText
import com.chaos.mangaties.core.component.text.TextState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    navigateToLogin: () -> Unit,
    navigateToSignUp: () -> Unit,
) {
    val image: Painter = painterResource(R.drawable.logo)
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            AppText(
                "Skip",
                color = MaterialTheme.colorScheme.primary,
                variant = TextState.BodyLarge,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
        Box(){
            Image(
                painter = image,
                contentDescription = "logo",
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        AppText(
            text = "MangaTies is the ultimate destination for manga lovers. " +
                    "Read, discover, and keep up with your favorite series anytime, anywhere.\n",
            variant = TextState.BodyMedium,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AppButton("Login", onClick = navigateToLogin, type = ButtonType.Primary, size = ButtonSize.Large)
            AppButton("Signup", onClick = navigateToSignUp, type = ButtonType.Secondary, size = ButtonSize.Large)
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}