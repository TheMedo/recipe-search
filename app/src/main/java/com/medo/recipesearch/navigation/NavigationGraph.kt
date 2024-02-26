package com.medo.recipesearch.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.medo.navigation.Route
import com.medo.recipesearch.ui.home.HomeView
import com.medo.recipesearch.ui.home.HomeViewModel

fun NavGraphBuilder.addHomeGraph(popBackStack: () -> Unit) {
    navigation(
        startDestination = Route.Home.label,
        route = Route.App.label,
    ) {
        composable(Route.Home.label) {
            val viewModel = hiltViewModel<HomeViewModel>()
            HomeView(viewModel)
        }
    }
}