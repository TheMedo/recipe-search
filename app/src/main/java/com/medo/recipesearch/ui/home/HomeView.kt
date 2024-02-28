package com.medo.recipesearch.ui.home

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.OfflineBolt
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.medo.data.local.model.Favorite
import com.medo.data.local.model.Recipe
import com.medo.data.local.model.RecipeWithIngredients
import com.medo.recipesearch.common.theme.generateRandomColor

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
) = Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
) {
    AnimatedSpacer(state.isSearchActive)

    HomeSearchBar(
        state = state,
        events = events,
    )

    when (state.isSearching) {
        true -> CircularProgressIndicator(modifier = Modifier.padding(top = 48.dp))
        else -> HomeGrid(
            state = state,
            events = events,
        )
    }
}

@Composable
private fun Loading() = Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center,
) {
    CircularProgressIndicator()
}

@Composable
private fun AnimatedSpacer(isSearchActive: Boolean) {
    val animatedHeight by animateDpAsState(
        targetValue = if (isSearchActive) 0.dp else 16.dp,
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeSearchBar(
    state: HomeState,
    events: (HomeEvent) -> Unit,
) = SearchBar(
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
    if (state.searchHistory.isNotEmpty()) {
        Text(
            "Search history",
            modifier = Modifier.padding(16.dp),
        )
    }

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

@Composable
private fun HomeGrid(
    state: HomeState,
    events: (HomeEvent) -> Unit,
) = LazyVerticalStaggeredGrid(
    columns = StaggeredGridCells.Fixed(2),
    modifier = Modifier
        .fillMaxHeight()
        .padding(vertical = 16.dp),
    contentPadding = PaddingValues(horizontal = 16.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalItemSpacing = 8.dp,
) {
    items(state.searchResults) {
        RecipeGridItem(
            item = it,
            isFavorite = state.favorites.isFavorite(it.recipe),
            events = events,
        )
    }
}

@Composable
private fun RecipeGridItem(
    item: RecipeWithIngredients,
    isFavorite: Boolean,
    events: (HomeEvent) -> Unit,
) = Card(
    modifier = Modifier
        .fillMaxSize()
        .clickable { events(HomeEvent.OpenItem(item)) },
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Box {
            AsyncImage(
                model = item.recipe.image,
                contentDescription = "Recipe image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height((LocalConfiguration.current.screenWidthDp / 2).dp - 32.dp),
                contentScale = ContentScale.Crop,
                placeholder = ColorPainter(generateRandomColor())
            )

            Icon(
                imageVector = when {
                    isFavorite -> Icons.Default.Favorite
                    else -> Icons.Outlined.FavoriteBorder
                },
                contentDescription = "Favorite recipe",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .clip(shape = RoundedCornerShape(8.dp))
                    .clickable { events(HomeEvent.ToggleFavorite(item, isFavorite)) }
                    .background(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f))
                    .padding(8.dp),
                tint = MaterialTheme.colorScheme.primaryContainer,
            )

            val dish = item.recipe.dishType?.firstOrNull()
            if (dish != null) {
                Text(
                    dish,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                        .clip(shape = RoundedCornerShape(8.dp))
                        .background(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f))
                        .padding(8.dp),
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.primaryContainer
                    ),
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        val cuisine = item.recipe.cuisineType?.firstOrNull()
        if (cuisine != null) {
            Text(
                cuisine,
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.labelSmall,
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            (item.recipe.title ?: ""),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            style = MaterialTheme.typography.titleLarge,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            val yield = item.recipe.yield?.toInt() ?: 0
            if (yield > 0) {
                TextWithIcon(
                    icon = Icons.Outlined.Restaurant,
                    text = yield.toString(),
                )
            }

            val time = item.recipe.totalTime?.toInt() ?: 0
            if (time > 0) {
                TextWithIcon(
                    icon = Icons.Outlined.Timer,
                    text = time.toString(),
                )
            }

            val calories = item.recipe.calories?.toInt() ?: 0
            if (calories > 0) {
                TextWithIcon(
                    icon = Icons.Outlined.OfflineBolt,
                    text = calories.toString(),
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun TextWithIcon(
    icon: ImageVector,
    text: String,
) = Row(
    verticalAlignment = Alignment.CenterVertically,
) {
    Icon(
        icon,
        contentDescription = null,
        modifier = Modifier.size(18.dp),
    )

    Spacer(modifier = Modifier.width(2.dp))

    Text(
        text,
        style = MaterialTheme.typography.labelMedium,
    )
}

private fun List<Favorite>.isFavorite(recipe: Recipe): Boolean =
    firstOrNull { it.recipeUri == recipe.uri }?.isFavorite == true