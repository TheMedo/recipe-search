package com.medo.recipesearch.ui.home

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.runtime.getValue
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
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
) {
    val animatedHeight by animateDpAsState(
        targetValue = if (state.isSearchActive) 0.dp else 16.dp,
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing
        ),
        label = "animatedHeight",
    )
    Spacer(
        modifier = Modifier
            .height(animatedHeight)
            .animateContentSize(),
    )

    SearchBar(
        query = state.searchQuery,
        onQueryChange = { events(HomeEvent.ChangeSearchQuery(it)) },
        onSearch = { events(HomeEvent.PerformSearch) },
        active = state.isSearchActive,
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
        Text(
            "Search history",
            modifier = Modifier.padding(16.dp),
        )
        state.searchHistory.map {
            ListItem(
                headlineContent = { Text(it) },
                trailingContent = {
                    IconButton(
                        onClick = { events(HomeEvent.DeleteSearchHistory(it)) }
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null)
                    }
                },
                modifier = Modifier
                    .clickable { events(HomeEvent.SelectSearchHistory(it)) }
                    .fillMaxWidth()
                    .padding(start = 16.dp)
            )
        }
    }

    when (state.isSearching) {
        true -> CircularProgressIndicator(modifier = Modifier.padding(top = 48.dp))
        else -> LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 16.dp),
        ) {
            items(state.searchResults) {
                ListItem(
                    headlineContent = { Text(it.recipe.title ?: "") },
                    supportingContent = { Text(it.recipe.shareAs ?: "") },
                    modifier = Modifier
                        .clickable { }
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }
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