package com.medo.recipesearch.ui.details

import androidx.lifecycle.SavedStateHandle
import com.medo.data.local.model.Recipe
import com.medo.data.local.model.RecipeWithIngredients
import com.medo.data.repository.RecipeRepository
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
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class DetailsViewModelTest : BaseTest() {

    private lateinit var viewModel: DetailsViewModel

    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var navigationController: NavigationController
    private lateinit var recipeRepository: RecipeRepository

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())

        savedStateHandle = mock<SavedStateHandle>()
        navigationController = mock<NavigationController>()
        recipeRepository = mock<RecipeRepository> {
            on { getRecipe(any()) } doReturn emptyFlow()
        }
    }

    @Test
    fun `given init - when id is empty - then navigate back`() = runTest {
        whenever(savedStateHandle.get<String>("id")).thenReturn("")

        viewModel = getViewModel()

        verify(navigationController).back()
    }

    @Test
    fun `given init - when id is not empty - then get recipe`() = runTest {
        whenever(savedStateHandle.get<String>("id")).thenReturn("uri")

        viewModel = getViewModel()

        verify(recipeRepository).getRecipe("uri")
    }

    @Test
    fun `given details - when back is pressed - then navigate back`() = runTest {
        viewModel = getViewModel()
        viewModel.onEvent(DetailsEvent.Close)

        verify(navigationController).back()
    }

    @Test
    fun `given details - when share is pressed - then share url`() = runTest {
        whenever(savedStateHandle.get<String>("id")).thenReturn("uri")
        whenever(recipeRepository.getRecipe("uri")).thenReturn(
            flowOf(
                RecipeWithIngredients(
                    recipe = Recipe(
                        uri = "uri",
                        shareAs = "shareUrl"
                    ),
                    ingredients = emptyList(),
                )
            )
        )

        viewModel = getViewModel()
        viewModel.onEvent(DetailsEvent.Share)

        verify(navigationController).share("shareUrl")
    }

    @After
    fun cleanup() = Dispatchers.resetMain()

    private fun getViewModel() = DetailsViewModel(
        coroutineDispatchers,
        savedStateHandle,
        navigationController,
        recipeRepository,
    )
}