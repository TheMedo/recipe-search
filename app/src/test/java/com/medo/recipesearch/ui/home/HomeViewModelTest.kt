package com.medo.recipesearch.ui.home

import com.medo.data.local.model.SearchHistory
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

            on { getFavorites() } doReturn emptyFlow()
        }

        viewModel = getViewModel()
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
        viewModel.onEvent(HomeEvent.ChangeSearchActive(true))

        assertEquals(viewModel.state.value.isSearchActive, true)
    }

    @Test
    fun `given search - when query is provided - then set state`() = runTest {
        viewModel.onEvent(HomeEvent.ChangeSearchQuery("query"))

        assertEquals(viewModel.state.value.searchQuery, "query")
    }

    @Test
    fun `given search - always - then set search not active`() = runTest {
        viewModel.onEvent(HomeEvent.PerformSearch)

        assertEquals(viewModel.state.value.isSearchActive, false)
    }

    @Test
    fun `given search - when query is empty - then never save to history`() = runTest {
        viewModel.onEvent(HomeEvent.ChangeSearchQuery(""))
        viewModel.onEvent(HomeEvent.PerformSearch)

        verify(searchHistoryRepository, never()).addSearchHistory("")
    }

    @Test
    fun `given search - when query is not empty - then save to history`() = runTest {
        viewModel.onEvent(HomeEvent.ChangeSearchQuery("query"))
        viewModel.onEvent(HomeEvent.PerformSearch)

        verify(searchHistoryRepository).addSearchHistory("query")
    }

    @Test
    fun `given search history - when selected - then update search query`() = runTest {
        viewModel.onEvent(HomeEvent.SelectSearchHistory(SearchHistory("query")))

        assertEquals(viewModel.state.value.searchQuery, "query")
    }

    @Test
    fun `given search history - when deleted - then remove search history`() = runTest {
        val searchHistory = SearchHistory("query")
        viewModel.onEvent(HomeEvent.DeleteSearchHistory(searchHistory))

        verify(searchHistoryRepository).removeSearchHistory(searchHistory)
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