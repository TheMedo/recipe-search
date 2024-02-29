package com.medo.recipesearch.ui.home

import com.medo.common.base.BaseViewModel
import com.medo.common.di.CoroutineDispatchers
import com.medo.data.local.model.Favorite
import com.medo.data.local.model.RecipeWithIngredients
import com.medo.data.local.model.SearchHistory
import com.medo.data.repository.RecipeRepository
import com.medo.data.repository.SearchHistoryRepository
import com.medo.data.repository.StorageKey
import com.medo.data.repository.StorageRepository
import com.medo.navigation.Destination
import com.medo.navigation.NavigationController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

sealed interface HomeEvent {
    data class ChangeSearchQuery(val query: String) : HomeEvent
    data class ChangeSearchActive(val active: Boolean) : HomeEvent
    data object PerformSearch : HomeEvent
    data object ToggleMenu : HomeEvent
    data object ToggleGrid : HomeEvent
    data class SelectSearchHistory(val value: SearchHistory) : HomeEvent
    data class DeleteSearchHistory(val value: SearchHistory) : HomeEvent
    data class OpenItem(val item: RecipeWithIngredients) : HomeEvent
    data class ToggleFavorite(val item: RecipeWithIngredients, val isFavorite: Boolean) : HomeEvent
}

data class HomeState(
    val isInitializing: Boolean = true,
    val searchQuery: String = "",
    val isSearchActive: Boolean = false,
    val searchHistory: List<SearchHistory> = emptyList(),
    val searchResults: List<RecipeWithIngredients> = emptyList(),
    val favorites: List<Favorite> = emptyList(),
    val isSearching: Boolean = false,
    val isGrid: Boolean = false,
    val showMenu: Boolean = false,
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    coroutineDispatchers: CoroutineDispatchers,
    private val navigationController: NavigationController,
    private val storageRepository: StorageRepository,
    private val searchHistoryRepository: SearchHistoryRepository,
    private val recipeRepository: RecipeRepository,
) : BaseViewModel<HomeState, HomeEvent>(HomeState(), coroutineDispatchers) {

    init {
        asyncMain {
            storageRepository.getBoolean(StorageKey.HasSeenWelcome).collect { hasSeenWelcome ->
                if (!hasSeenWelcome) {
                    navigationController.navigateTo(Destination.Welcome)
                    delay(1000)
                    setState(currentState.copy(isInitializing = false))
                } else {
                    setState(currentState.copy(isInitializing = false))
                }
            }
        }
        asyncMain {
            storageRepository.getBoolean(StorageKey.IsGrid).collect {
                setState(currentState.copy(isGrid = it))
            }
        }
        asyncMain {
            searchHistoryRepository.getSearchHistory().collect {
                setState(currentState.copy(searchHistory = it))
            }
        }
        asyncMain {
            recipeRepository.getCurrentSearchResults().collect {
                setState(currentState.copy(searchResults = it))
            }
        }
        asyncMain {
            recipeRepository.getFavorites().collect {
                setState(currentState.copy(favorites = it))
            }
        }
    }

    override fun onEvent(event: HomeEvent) = when (event) {
        is HomeEvent.ChangeSearchActive -> onChangeSearchActive(event.active)
        is HomeEvent.ChangeSearchQuery -> onChangeSearchQuery(event.query)
        HomeEvent.PerformSearch -> onPerformSearch()
        HomeEvent.ToggleMenu -> onToggleMenu()
        HomeEvent.ToggleGrid -> onToggleGrid()
        is HomeEvent.SelectSearchHistory -> onSelectSearchHistory(event.value)
        is HomeEvent.DeleteSearchHistory -> onDeleteSearchHistory(event.value)
        is HomeEvent.OpenItem -> onOpenItem(event.item)
        is HomeEvent.ToggleFavorite -> onToggleFavorite(event.item, event.isFavorite)
    }

    private fun onChangeSearchActive(active: Boolean) = setState(currentState.copy(isSearchActive = active))

    private fun onChangeSearchQuery(query: String) {
        val isCurrentQueryEmpty = currentState.searchQuery.isEmpty()

        setState(currentState.copy(searchQuery = query))

        if (query.isEmpty() && isCurrentQueryEmpty) {
            setState(currentState.copy(isSearchActive = false))
        }
    }

    private fun onPerformSearch() {
        setState(currentState.copy(isSearchActive = false))

        if (currentState.searchQuery.isEmpty()) return

        setState(currentState.copy(isSearching = true))

        asyncIo {
            searchHistoryRepository.addSearchHistory(currentState.searchQuery)
        }

        asyncMain {
            val result = awaitIo {
                recipeRepository.searchRecipes(currentState.searchQuery)
            }

            if (result == null) {
                navigationController.snackbar("Something went wrong, cannot search for recipes.")
            }

            setState(currentState.copy(isSearching = false))
        }
    }

    private fun onToggleMenu() = setState(currentState.copy(showMenu = !currentState.showMenu))

    private fun onToggleGrid() {
        asyncIo {
            storageRepository.setBoolean(StorageKey.IsGrid, !currentState.isGrid)
        }
    }

    private fun onSelectSearchHistory(value: SearchHistory) {
        setState(currentState.copy(searchQuery = value.query))
        onPerformSearch()
    }

    private fun onDeleteSearchHistory(value: SearchHistory) {
        asyncIo {
            searchHistoryRepository.removeSearchHistory(value)
        }
    }

    private fun onOpenItem(item: RecipeWithIngredients) {
    }

    private fun onToggleFavorite(item: RecipeWithIngredients, isFavorite: Boolean) {
        asyncIo {
            recipeRepository.updateFavorite(
                Favorite(
                    recipeUri = item.recipe.uri,
                    isFavorite = !isFavorite
                )
            )
        }
    }
}