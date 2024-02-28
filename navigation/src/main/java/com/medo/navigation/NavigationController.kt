package com.medo.navigation

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

interface NavigationController {
    val events: SharedFlow<Destination>

    fun navigateTo(destination: Destination)

    fun snackbar(message: String)
}

class ComposeNavigationController @Inject constructor() : NavigationController {

    private val _events = MutableSharedFlow<Destination>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    override val events: SharedFlow<Destination>
        get() = _events.asSharedFlow()

    override fun navigateTo(destination: Destination) {
        _events.tryEmit(destination)
    }

    override fun snackbar(message: String) {
        _events.tryEmit(Destination.Snackbar(message))
    }
}