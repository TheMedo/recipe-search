package com.medo.recipesearch.ui.home

import com.medo.common.base.BaseViewModel
import com.medo.common.di.CoroutineDispatchers
import com.medo.data.local.model.RecipeWithIngredients
import com.medo.data.repository.RecipeRepository
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
    data class SelectSearchHistory(val value: String) : HomeEvent
    data class DeleteSearchHistory(val value: String) : HomeEvent
}

data class HomeState(
    val isInitializing: Boolean = true,
    val searchQuery: String = "",
    val isSearchActive: Boolean = false,
    val searchHistory: List<String> = emptyList(),
    val searchResults: List<RecipeWithIngredients> = emptyList(),
    val isSearching: Boolean = false,
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    coroutineDispatchers: CoroutineDispatchers,
    private val navigationController: NavigationController,
    private val storageRepository: StorageRepository,
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
            storageRepository.getSearchHistory().collect {
                setState(currentState.copy(searchHistory = it.reversed()))
            }
        }
        asyncMain {
            recipeRepository.getCurrentSearchResults().collect {
                setState(currentState.copy(searchResults = it))
            }
        }
    }

    override fun onEvent(event: HomeEvent) = when (event) {
        is HomeEvent.ChangeSearchActive -> onChangeSearchActive(event.active)
        is HomeEvent.ChangeSearchQuery -> onChangeSearchQuery(event.query)
        HomeEvent.PerformSearch -> onPerformSearch()
        is HomeEvent.SelectSearchHistory -> onSelectSearchHistory(event.value)
        is HomeEvent.DeleteSearchHistory -> onDeleteSearchHistory(event.value)
    }

    private fun onChangeSearchActive(active: Boolean) = setState(currentState.copy(isSearchActive = active))

    private fun onChangeSearchQuery(query: String) = setState(currentState.copy(searchQuery = query))

    private fun onPerformSearch() {
        setState(currentState.copy(isSearchActive = false))

        if (currentState.searchQuery.isEmpty()) return

        setState(currentState.copy(isSearching = true))

        asyncIo {
            storageRepository.addToSearchHistory(currentState.searchQuery)
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

    private fun onSelectSearchHistory(value: String) {
        setState(currentState.copy(searchQuery = value))
        onPerformSearch()
    }

    private fun onDeleteSearchHistory(value: String) {
        asyncIo {
            storageRepository.removeFromSearchHistory(value)
        }
    }
}