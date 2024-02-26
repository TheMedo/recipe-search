package com.medo.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseViewModel<S, E>(
    initialState: S,
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state = _state.asStateFlow()

    protected val currentState: S
        get() = _state.value

    abstract fun onEvent(event: E)

    protected fun setState(newState: S) {
        _state.value = newState
    }

    protected inline fun async(
        crossinline block: suspend () -> Unit,
    ) = viewModelScope.launch {
        block()
    }

    protected suspend inline fun <T> await(
        crossinline block: suspend () -> T,
    ): T = withContext(viewModelScope.coroutineContext) {
        block()
    }
}