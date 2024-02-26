package com.medo.welcome.ui

import com.medo.common.base.BaseViewModel
import com.medo.navigation.NavigationController
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed interface WelcomeEvent

data class WelcomeState(
    private val empty: String = "",
)

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val navigationController: NavigationController,
) : BaseViewModel<WelcomeState, WelcomeEvent>(WelcomeState()) {

    override fun onEvent(event: WelcomeEvent) {
        TODO("Not yet implemented")
    }
}