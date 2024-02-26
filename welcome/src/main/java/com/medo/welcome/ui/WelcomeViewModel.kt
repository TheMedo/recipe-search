package com.medo.welcome.ui

import com.medo.common.base.BaseViewModel
import com.medo.navigation.NavigationController
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val navigationController: NavigationController,
) : BaseViewModel() {

}