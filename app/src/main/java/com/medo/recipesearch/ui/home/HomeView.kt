package com.medo.recipesearch.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeView(viewModel: HomeViewModel) {
    val state = viewModel.state.collectAsState().value

    when (state.isInitializing) {
        true -> Loading()
        else -> Home(state) { viewModel.onEvent(it) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Home(
    state: HomeState,
    events: (HomeEvent) -> Unit,
) = Column(
    horizontalAlignment = Alignment.CenterHorizontally,
) {
    SearchBar(
        query = state.searchQuery,
        onQueryChange = { events(HomeEvent.ChangeSearchQuery(it)) },
        onSearch = { events(HomeEvent.ChangeSearchActive(false)) },
        active = state.searchActive,
        onActiveChange = { events(HomeEvent.ChangeSearchActive(it)) },
        placeholder = { Text("Search recipes") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
            when {
                state.searchQuery.isEmpty() -> {}
                else -> IconButton(
                    onClick = { events(HomeEvent.ChangeSearchQuery("")) }
                ) {
                    Icon(Icons.Default.Clear, contentDescription = null)
                }
            }
        },
    ) {
        repeat(4) {
            ListItem(
                headlineContent = { Text("Suggestion $it") },
                leadingContent = { Icon(Icons.Default.Refresh, contentDescription = null) },
                modifier = Modifier
                    .clickable {
                    }
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            )
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