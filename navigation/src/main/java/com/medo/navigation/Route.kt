package com.medo.navigation

sealed class Destination(val label: String) {
    data object Home : Route("home")
    data object Welcome : Route("welcome")
    data class Details(val id: String) : Route("details/$id")
}

sealed class Route(val label: String) {
    data object App : Route("app")
    data object Welcome : Route("welcome")
}