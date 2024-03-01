package com.medo.recipesearch.ui.home

import com.medo.data.local.model.Recipe
import com.medo.data.local.model.RecipeWithIngredients
import com.medo.data.local.model.SearchHistory
import com.medo.data.remote.model.Links
import com.medo.data.remote.model.Next
import com.medo.data.remote.model.SearchRecipesResponse
import com.medo.data.repository.RecipeRepository
import com.medo.data.repository.SearchHistoryRepository
import com.medo.data.repository.StorageKey
import com.medo.data.repository.StorageRepository
import com.medo.navigation.Destination
import com.medo.navigation.NavigationController
import com.medo.recipesearch.base.BaseTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest : BaseTest() {

    private lateinit var viewModel: HomeViewModel

    private lateinit var navigationController: NavigationController
    private lateinit var storageRepository: StorageRepository
    private lateinit var searchHistoryRepository: SearchHistoryRepository
    private lateinit var recipeRepository: RecipeRepository

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())

        navigationController = mock<NavigationController>()
        storageRepository = mock<StorageRepository> {
            on { getBoolean(any()) } doReturn emptyFlow()
        }
        searchHistoryRepository = mock<SearchHistoryRepository> {
            on { getSearchHistory() } doReturn emptyFlow()
        }
        recipeRepository = mock<RecipeRepository> {
            on { getCurrentSearchResults() } doReturn emptyFlow()
        }
    }

    @Test
    fun `given init - when has not seen welcome - then navigate to welcome`() = runTest {
        whenever(storageRepository.getBoolean(StorageKey.HasSeenWelcome)).thenReturn(flowOf(false))

        viewModel = getViewModel()

        verify(navigationController).navigateTo(Destination.Welcome)
    }

    @Test
    fun `given init - when has seen welcome - then set initializing false`() = runTest {
        whenever(storageRepository.getBoolean(StorageKey.HasSeenWelcome)).thenReturn(flowOf(true))

        viewModel = getViewModel()

        assertEquals(viewModel.state.value.isInitializing, false)
    }

    @Test
    fun `given search - when change to active - then set state`() = runTest {
        viewModel = getViewModel()
        viewModel.onEvent(HomeEvent.ChangeSearchActive(true))

        assertEquals(viewModel.state.value.isSearchActive, true)
    }

    @Test
    fun `given search - when query is provided - then set state`() = runTest {
        viewModel = getViewModel()
        viewModel.onEvent(HomeEvent.ChangeSearchQuery("query"))

        assertEquals(viewModel.state.value.searchQuery, "query")
    }

    @Test
    fun `given search - always - then set search not active`() = runTest {
        viewModel = getViewModel()
        viewModel.onEvent(HomeEvent.PerformSearch)

        assertEquals(viewModel.state.value.isSearchActive, false)
    }

    @Test
    fun `given search - when query is empty - then never save to history`() = runTest {
        viewModel = getViewModel()
        viewModel.onEvent(HomeEvent.ChangeSearchQuery(""))
        viewModel.onEvent(HomeEvent.PerformSearch)

        verify(searchHistoryRepository, never()).addSearchHistory("")
    }

    @Test
    fun `given search - when query is not empty - then save to history`() = runTest {
        viewModel = getViewModel()
        viewModel.onEvent(HomeEvent.ChangeSearchQuery("query"))
        viewModel.onEvent(HomeEvent.PerformSearch)

        verify(searchHistoryRepository).addSearchHistory("query")
    }

    @Test
    fun `given search history - when selected - then update search query`() = runTest {
        viewModel = getViewModel()
        viewModel.onEvent(HomeEvent.SelectSearchHistory(SearchHistory("query")))

        assertEquals(viewModel.state.value.searchQuery, "query")
    }

    @Test
    fun `given search history - when deleted - then remove search history`() = runTest {
        viewModel = getViewModel()

        val searchHistory = SearchHistory("query")
        viewModel.onEvent(HomeEvent.DeleteSearchHistory(searchHistory))

        verify(searchHistoryRepository).removeSearchHistory(searchHistory)
    }

    @Test
    fun `given no search query - when perform search - then do not call endpoint`() = runTest {
        viewModel = getViewModel()
        viewModel.onEvent(HomeEvent.ChangeSearchQuery(""))
        viewModel.onEvent(HomeEvent.PerformSearch)

        verify(recipeRepository, never()).searchRecipes("")
    }

    @Test
    fun `given search query - when perform search - then call endpoint`() = runTest {
        viewModel = getViewModel()
        viewModel.onEvent(HomeEvent.ChangeSearchQuery("pancakes"))
        viewModel.onEvent(HomeEvent.PerformSearch)

        verify(recipeRepository).searchRecipes("pancakes")
    }

    @Test
    fun `always - when perform search - set search not active`() = runTest {
        viewModel = getViewModel()
        viewModel.onEvent(HomeEvent.PerformSearch)

        assertEquals(viewModel.state.value.isSearchActive, false)
    }

    @Test
    fun `given menu not shown - when toggle menu - then show menu`() = runTest {
        viewModel = getViewModel()
        viewModel.onEvent(HomeEvent.ToggleMenu)

        assertEquals(viewModel.state.value.showMenu, true)
    }

    @Test
    fun `given menu shown - when toggle menu - then hide menu`() = runTest {
        viewModel = getViewModel()
        viewModel.onEvent(HomeEvent.ToggleMenu)
        viewModel.onEvent(HomeEvent.ToggleMenu)

        assertEquals(viewModel.state.value.showMenu, false)
    }

    @Test
    fun `given view mode list - when toggle view mode - then save grid`() = runTest {
        whenever(storageRepository.getBoolean(StorageKey.IsList)).thenReturn(flowOf(true))

        viewModel = getViewModel()
        viewModel.onEvent(HomeEvent.ToggleViewMode)

        verify(storageRepository).setBoolean(StorageKey.IsList, false)
    }

    @Test
    fun `given view mode grid - when toggle view mode - then save list`() = runTest {
        whenever(storageRepository.getBoolean(StorageKey.IsList)).thenReturn(flowOf(false))

        viewModel = getViewModel()
        viewModel.onEvent(HomeEvent.ToggleViewMode)

        verify(storageRepository).setBoolean(StorageKey.IsList, true)
    }

    @Test
    fun `given search results - when item click - then open details`() = runTest {
        viewModel = getViewModel()

        val url = "https://www.youtube.com/watch?v=u436QYpZpmw&ab_channel=PitchMeeting"
        viewModel.onEvent(
            HomeEvent.OpenItem(
                RecipeWithIngredients(
                    recipe = Recipe(uri = url),
                    ingredients = emptyList()
                )
            )
        )

        val encodedUri = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
        verify(navigationController).navigateTo(Destination.Details(encodedUri))
    }

    @Test
    fun `given search results - when scroll to end of list - then load more`() = runTest {
        val response = SearchRecipesResponse(
            from = 0,
            to = 20,
            count = 1000,
            links = Links(
                next = Next(
                    href = "next"
                )
            )
        )
        whenever(recipeRepository.searchRecipes("pancakes")).thenReturn(response)

        viewModel = getViewModel()
        viewModel.onEvent(HomeEvent.ChangeSearchQuery("pancakes"))
        viewModel.onEvent(HomeEvent.PerformSearch)
        viewModel.onEvent(HomeEvent.LoadMore)

        verify(recipeRepository).searchRecipesNextPage("next", 20)
    }

    @Test
    fun `given all search results - when scroll to end of list - then do not load more`() = runTest {
        val response = SearchRecipesResponse(
            from = 0,
            to = 2,
            count = 2,
            links = Links(
                next = Next(
                    href = "next"
                )
            )
        )
        whenever(recipeRepository.searchRecipes("pancakes")).thenReturn(response)
        whenever(recipeRepository.getCurrentSearchResults()).thenReturn(
            flowOf(
                listOf(
                    RecipeWithIngredients(
                        recipe = Recipe(uri = "1", index = 1),
                        ingredients = emptyList(),
                    ),
                    RecipeWithIngredients(
                        recipe = Recipe(uri = "2", index = 2),
                        ingredients = emptyList(),
                    ),
                    RecipeWithIngredients(
                        recipe = Recipe(uri = "3", index = 3),
                        ingredients = emptyList(),
                    ),
                )
            )
        )

        viewModel = getViewModel()
        viewModel.onEvent(HomeEvent.ChangeSearchQuery("pancakes"))
        viewModel.onEvent(HomeEvent.PerformSearch)
        viewModel.onEvent(HomeEvent.LoadMore)

        verify(recipeRepository, never()).searchRecipesNextPage("next", 2)
    }

    @After
    fun cleanup() = Dispatchers.resetMain()

    private fun getViewModel() = HomeViewModel(
        coroutineDispatchers,
        navigationController,
        storageRepository,
        searchHistoryRepository,
        recipeRepository,
    )
}