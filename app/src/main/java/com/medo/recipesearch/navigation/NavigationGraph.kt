package com.medo.recipesearch.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType.Companion.StringType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.medo.navigation.Destination
import com.medo.navigation.Route
import com.medo.recipesearch.ui.details.DetailsView
import com.medo.recipesearch.ui.details.DetailsViewModel
import com.medo.recipesearch.ui.home.HomeView
import com.medo.recipesearch.ui.home.HomeViewModel

fun NavGraphBuilder.addHomeGraph() {
    navigation(
        startDestination = Destination.Home.label,
        route = Route.App.label,
    ) {
        composable(Destination.Home.label) {
            val viewModel = hiltViewModel<HomeViewModel>()
            HomeView(viewModel)
        }

        composable(
            route = "details/{id}",
            arguments = listOf(navArgument("id") { type = StringType })
        ) {
            val viewModel = hiltViewModel<DetailsViewModel>()
            DetailsView(viewModel)
        }
    }
}