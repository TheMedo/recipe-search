package com.medo.common.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel<S, E>(
    initialState: S,
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state = _state.asStateFlow()

    protected var currentState: S = initialState

    abstract fun onEvent(event: E)

    protected fun setState(newState: S) {
        _state.value = newState
        currentState = newState
    }
}