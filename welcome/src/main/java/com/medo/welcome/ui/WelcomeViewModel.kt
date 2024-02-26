package com.medo.welcome.ui

import androidx.lifecycle.viewModelScope
import com.medo.common.base.BaseViewModel
import com.medo.data.repository.StorageKey
import com.medo.data.repository.StorageRepository
import com.medo.navigation.Destination
import com.medo.navigation.NavigationController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface WelcomeEvent {
    data object GetStarted : WelcomeEvent
}

data class WelcomeState(
    private val empty: String = "",
)

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val navigationController: NavigationController,
    private val storageRepository: StorageRepository,
) : BaseViewModel<WelcomeState, WelcomeEvent>(WelcomeState()) {

    override fun onEvent(event: WelcomeEvent) {
        when (event) {
            WelcomeEvent.GetStarted -> onGetStarted()
        }
    }

    private fun onGetStarted() {
        viewModelScope.launch {
            storageRepository.setBoolean(StorageKey.HasSeenWelcome, true)
            navigationController.navigateTo(Destination.Home)
        }
    }
}