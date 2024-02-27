package com.medo.recipesearch.ui.home

import com.medo.common.base.BaseViewModel
import com.medo.common.di.CoroutineDispatchers
import com.medo.data.repository.StorageKey
import com.medo.data.repository.StorageRepository
import com.medo.navigation.Destination
import com.medo.navigation.NavigationController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

sealed interface HomeEvent {
    data object ChangeState : HomeEvent
}

data class HomeState(
    val isInitializing: Boolean = true,
    val title: String = "Home",
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    coroutineDispatchers: CoroutineDispatchers,
    private val navigationController: NavigationController,
    private val storageRepository: StorageRepository,
) : BaseViewModel<HomeState, HomeEvent>(HomeState(), coroutineDispatchers) {

    init {
        asyncMain {
            storageRepository.getBoolean(StorageKey.HasSeenWelcome)
                .collect { hasSeenWelcome ->
                    if (!hasSeenWelcome) {
                        navigationController.navigateTo(Destination.Welcome)
                        delay(1000)
                        setState(currentState.copy(isInitializing = false))
                    } else {
                        setState(currentState.copy(isInitializing = false))
                    }
                }
        }
    }

    override fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.ChangeState -> setState(currentState.copy(title = "New home"))
        }
    }

}