package com.medo.recipesearch.ui.details

import androidx.lifecycle.SavedStateHandle
import com.medo.common.base.BaseViewModel
import com.medo.common.di.CoroutineDispatchers
import com.medo.data.local.model.RecipeWithIngredients
import com.medo.data.repository.RecipeRepository
import com.medo.navigation.NavigationController
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed interface DetailsEvent {
    data object Close : DetailsEvent
    data object Share : DetailsEvent
}

data class DetailsState(
    val data: RecipeWithIngredients? = null,
)

@HiltViewModel
class DetailsViewModel @Inject constructor(
    coroutineDispatchers: CoroutineDispatchers,
    savedStateHandle: SavedStateHandle,
    private val navigationController: NavigationController,
    private val recipeRepository: RecipeRepository,
) : BaseViewModel<DetailsState, DetailsEvent>(DetailsState(), coroutineDispatchers) {

    init {
        val uri = savedStateHandle.get<String>("id")
        if (uri.isNullOrEmpty()) {
            navigationController.back()
        } else {
            asyncMain {
                recipeRepository.getRecipe(uri).collect {
                    setState(currentState.copy(data = it))
                }
            }
        }
    }

    override fun onEvent(event: DetailsEvent) = when (event) {
        DetailsEvent.Close -> navigationController.back()
        DetailsEvent.Share -> navigationController.share(currentState.data?.recipe?.shareAs ?: "")
    }
}