package com.medo.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medo.common.di.CoroutineDispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseViewModel<S, E>(
    initialState: S,
    protected val coroutineDispatchers: CoroutineDispatchers,
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state = _state.asStateFlow()

    protected val currentState: S
        get() = _state.value

    abstract fun onEvent(event: E)

    protected fun setState(newState: S) {
        _state.value = newState
    }

    protected inline fun asyncMain(
        crossinline block: suspend () -> Unit,
    ) = viewModelScope.launch(coroutineDispatchers.main) {
        block()
    }

    protected inline fun asyncIo(
        crossinline block: suspend () -> Unit,
    ) = viewModelScope.launch(coroutineDispatchers.io) {
        block()
    }

    protected suspend inline fun <T> awaitMain(
        crossinline block: suspend () -> T,
    ): T = withContext(coroutineDispatchers.main) {
        block()
    }

    protected suspend inline fun <T> awaitIo(
        crossinline block: suspend () -> T,
    ): T = withContext(coroutineDispatchers.io) {
        block()
    }
}