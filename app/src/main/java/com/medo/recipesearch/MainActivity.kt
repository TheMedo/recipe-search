package com.medo.recipesearch

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
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
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationController: NavigationController

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecipeSearchTheme {
                val navController = rememberNavController()
                val scope = rememberCoroutineScope()
                val snackbarHostState = remember { SnackbarHostState() }

                LaunchedEffect("navigation") {
                    navigationController.events.onEach { destination ->
                        when (destination) {
                            is Destination.Back -> navController.popBackStack()

                            is Destination.Snackbar -> scope.launch {
                                snackbarHostState.showSnackbar(destination.message)
                            }

                            is Destination.Share -> startActivity(
                                Intent.createChooser(
                                    Intent().apply {
                                        action = Intent.ACTION_SEND
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_TEXT, destination.text)
                                    }, "Share recipe"
                                )
                            )

                            else -> navController.navigate(destination.label) {
                                when (destination) {
                                    Destination.Welcome, Destination.Home -> popUpTo(0)
                                    else -> popUpTo(destination.label)
                                }
                            }
                        }
                    }.launchIn(this)
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Route.App.label,
                    ) {
                        addHomeGraph()
                        addWelcomeGraph { navController.popBackStack() }
                    }
                }
            }
        }
    }
}