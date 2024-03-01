package com.medo.welcome.ui

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun WelcomeView(viewModel: WelcomeViewModel) = Column(
    modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp, vertical = 48.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
) {
    Text(
        "Welcome to",
        style = MaterialTheme.typography.headlineSmall,
    )

    Text(
        "Recipe Search",
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.ExtraBold,
    )

    Spacer(modifier = Modifier.weight(1f))

    PulseLogo()

    Spacer(modifier = Modifier.weight(1f))

    Button(
        onClick = { viewModel.onEvent(WelcomeEvent.GetStarted) },
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            "Get Started",
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Composable
private fun PulseLogo() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")

    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000),
            repeatMode = RepeatMode.Reverse
        ), label = "pulse"
    )

    Icon(
        Icons.Default.RestaurantMenu,
        contentDescription = "Logo",
        modifier = Modifier
            .size(128.dp)
            .scale(scale),
    )
}