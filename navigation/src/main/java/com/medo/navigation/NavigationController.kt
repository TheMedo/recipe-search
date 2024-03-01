package com.medo.navigation

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

interface NavigationController {
    val events: SharedFlow<Destination>

    fun navigateTo(destination: Destination)

    fun back()

    fun snackbar(message: String)

    fun share(message: String)
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

    override fun back() = navigateTo(Destination.Back)

    override fun snackbar(message: String) = navigateTo(Destination.Snackbar(message))

    override fun share(message: String) = navigateTo(Destination.Share(message))
}