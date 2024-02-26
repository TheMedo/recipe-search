package com.medo.recipesearch.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun HomeView(viewModel: HomeViewModel) {
    val state = viewModel.state.collectAsState().value

    when (state.isInitializing) {
        true -> Loading()
        else -> Home(state) { viewModel.onEvent(it) }
    }
}

@Composable
private fun Home(
    state: HomeState,
    events: (HomeEvent) -> Unit,
) {
    Column {
        Text(state.title)
        Button(onClick = { events(HomeEvent.ChangeState) }) {
            Text("Change State")
        }
    }
}

@Composable
private fun Loading() = Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center,
) {
    CircularProgressIndicator()
}