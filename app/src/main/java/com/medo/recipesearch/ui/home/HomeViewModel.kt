package com.medo.recipesearch.ui.home

import com.medo.navigation.NavigationController
import com.medo.recipesearch.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val navigationController: NavigationController,
) : BaseViewModel() {

}