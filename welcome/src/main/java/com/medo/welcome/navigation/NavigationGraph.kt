package com.medo.welcome.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.medo.navigation.Destination
import com.medo.navigation.Route
import com.medo.welcome.ui.WelcomeView
import com.medo.welcome.ui.WelcomeViewModel

fun NavGraphBuilder.addWelcomeGraph(popBackStack: () -> Unit) {
    navigation(
        startDestination = Destination.Welcome.label,
        route = Route.Welcome.label,
    ) {
        composable(Destination.Welcome.label) {
            val viewModel = hiltViewModel<WelcomeViewModel>()
            WelcomeView(viewModel)
        }
    }
}