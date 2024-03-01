package com.medo.recipesearch.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.medo.data.local.model.RecipeWithIngredients
import com.medo.recipesearch.common.theme.generateRandomColor
import com.medo.recipesearch.common.view.Loading
import me.onebone.toolbar.ExperimentalToolbarApi
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.ToolbarWithFabScaffold
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@Composable
fun DetailsView(viewModel: DetailsViewModel) {
    val state = viewModel.state.collectAsState().value

    when (state.data) {
        null -> Loading()
        else -> Details(state.data) { viewModel.onEvent(it) }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalToolbarApi::class)
@Composable
private fun Details(
    data: RecipeWithIngredients,
    events: (DetailsEvent) -> Unit,
) = ToolbarWithFabScaffold(
    modifier = Modifier.fillMaxSize(),
    state = rememberCollapsingToolbarScaffoldState(),
    scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
    toolbar = {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(256.dp)
                .parallax(0.3f),
        ) {
            AsyncImage(
                model = data.recipe.image,
                contentDescription = "Recipe image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillWidth,
                placeholder = ColorPainter(generateRandomColor())
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x88000000)),
            )
        }

        TopAppBar(
            title = {
                Spacer(modifier = Modifier.size(0.dp))
            },
            colors = topAppBarColors(
                containerColor = Color.Transparent,
            ),
            navigationIcon = {
                IconButton(onClick = { events(DetailsEvent.Close) }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            },
        )

        Text(
            text = data.recipe.title ?: "",
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 56.dp)
                .road(
                    whenCollapsed = Alignment.CenterStart,
                    whenExpanded = Alignment.BottomCenter
                ),
            style = MaterialTheme.typography.headlineSmall.copy(color = MaterialTheme.colorScheme.onPrimary),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    },
    fab = {
        FloatingActionButton(
            onClick = { events(DetailsEvent.Share) },
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share",
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    },
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Row(
            modifier = Modifier.wrapContentWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            data.recipe.dishType?.firstOrNull()?.let {
                LabelText(it)
            }

            data.recipe.cuisineType?.firstOrNull()?.let {
                LabelText(it)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.SpaceEvenly,
        ) {
            data.recipe.yield?.toInt()?.let {
                NumberText(
                    value = it,
                    text = "serving"
                )
            }

            data.recipe.totalTime?.toInt()?.takeIf { it in 1..300 }?.let {
                NumberText(
                    value = it,
                    text = "minute"
                )
            }

            data.recipe.calories?.toInt()?.let {
                NumberText(
                    value = it,
                    text = "calorie"
                )
            }
        }

        data.recipe.ingredientLines?.takeIf { it.isNotEmpty() }?.let {
            ListSection(
                title = "Ingredients",
                items = it
            )
        }

        data.recipe.instructions?.takeIf { it.isNotEmpty() }?.let {
            ListSection(
                title = "Instructions",
                items = it
            )
        }

        data.recipe.dietLabels?.takeIf { it.isNotEmpty() }?.let {
            ListSection(
                title = "Diet",
                items = it
            )
        }

        data.recipe.healthLabels?.takeIf { it.isNotEmpty() }?.let {
            ListSection(
                title = "Labels",
                items = it
            )
        }

        data.recipe.cautions?.takeIf { it.isNotEmpty() }?.let {
            ListSection(
                title = "Allergens",
                items = it
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun NumberText(value: Int, text: String) = Column(
    horizontalAlignment = Alignment.CenterHorizontally,
) {
    Text(
        "$value",
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
    )
    Text(
        "$text${if (value == 1) "" else "s"}",
        style = MaterialTheme.typography.labelLarge,
    )
}

@Composable
private fun LabelText(text: String) = Text(
    text.capitalize(Locale.current),
    modifier = Modifier
        .clip(shape = RoundedCornerShape(8.dp))
        .background(color = MaterialTheme.colorScheme.onPrimaryContainer)
        .padding(8.dp),
    style = MaterialTheme.typography.labelLarge.copy(
        color = MaterialTheme.colorScheme.primaryContainer
    ),
)

@Composable
private fun ListSection(title: String, items: List<String>) = Column(
    modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
) {
    Text(
        title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
    )

    Spacer(modifier = Modifier.height(8.dp))

    items.map {
        Text(
            "• $it",
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}
