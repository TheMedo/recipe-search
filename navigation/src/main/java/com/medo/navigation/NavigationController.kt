package com.medo.navigation

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Singleton

interface NavigationController {
    fun getEvents(): SharedFlow<Destination>

    fun navigateTo(destination: Destination)
}

@Singleton
class ComposeNavigationController : NavigationController {

    private val _events = MutableSharedFlow<Destination>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    override fun getEvents() = _events.asSharedFlow()

    override fun navigateTo(destination: Destination) {
        _events.tryEmit(destination)
    }
}