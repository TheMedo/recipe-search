package com.medo.welcome.ui

import com.medo.common.base.BaseViewModel
import com.medo.common.di.CoroutineDispatchers
import com.medo.data.repository.StorageKey
import com.medo.data.repository.StorageRepository
import com.medo.navigation.Destination
import com.medo.navigation.NavigationController
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed interface WelcomeEvent {
    data object GetStarted : WelcomeEvent
}

data class WelcomeState(
    private val empty: String = "",
)

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    coroutineDispatchers: CoroutineDispatchers,
    private val navigationController: NavigationController,
    private val storageRepository: StorageRepository,
) : BaseViewModel<WelcomeState, WelcomeEvent>(WelcomeState(), coroutineDispatchers) {

    override fun onEvent(event: WelcomeEvent) {
        when (event) {
            WelcomeEvent.GetStarted -> onGetStarted()
        }
    }

    private fun onGetStarted() {
        asyncIo {
            storageRepository.setBoolean(StorageKey.HasSeenWelcome, true)
        }
        asyncMain {
            navigationController.navigateTo(Destination.Home)
        }
    }
}