package com.medo.recipesearch.ui.home

import com.medo.common.base.BaseViewModel
import com.medo.navigation.Destination
import com.medo.navigation.NavigationController
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed interface HomeEvent {
    data object OpenWelcome : HomeEvent
    data object ChangeState : HomeEvent
}

data class HomeState(
    val title: String = "Home",
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val navigationController: NavigationController,
) : BaseViewModel<HomeState, HomeEvent>(HomeState()) {

    override fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.OpenWelcome -> navigationController.navigateTo(Destination.Welcome)
            HomeEvent.ChangeState -> setState(currentState.copy(title = "New home"))
        }
    }

}