package com.medo.recipesearch.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

@Composable
fun HomeView(viewModel: HomeViewModel) {
    val state = viewModel.state.collectAsState().value

    Column {
        Text(state.title)
        Button(onClick = { viewModel.onEvent(HomeEvent.OpenWelcome) }) {
            Text("Open Welcome")
        }
        Button(onClick = { viewModel.onEvent(HomeEvent.ChangeState) }) {
            Text("Change State")
        }
    }
}