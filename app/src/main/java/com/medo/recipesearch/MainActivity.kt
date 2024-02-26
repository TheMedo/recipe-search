package com.medo.recipesearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.medo.navigation.Destination
import com.medo.navigation.NavigationController
import com.medo.navigation.Route
import com.medo.recipesearch.common.theme.RecipeSearchTheme
import com.medo.recipesearch.navigation.addHomeGraph
import com.medo.welcome.navigation.addWelcomeGraph
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationController: NavigationController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecipeSearchTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    MainNavigation(navController = navController, navigationController = navigationController)
                }
            }
        }
    }
}

@Composable
fun MainNavigation(
    navController: NavHostController,
    navigationController: NavigationController,
) {
    LaunchedEffect("navigation") {
        navigationController.events.onEach {
            navController.navigate(it.label) {
                when (it) {
                    Destination.Welcome -> popUpTo(0)
                    else -> popUpTo(it.label)
                }
            }
        }.launchIn(this)
    }

    NavHost(
        navController = navController,
        startDestination = Route.App.label,
    ) {
        addHomeGraph { navController.popBackStack() }
        addWelcomeGraph { navController.popBackStack() }
    }
}