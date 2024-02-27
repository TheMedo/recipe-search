package com.medo.navigation

sealed class Destination(val label: String) {
    data object Home : Destination("home")
    data object Welcome : Destination("welcome")
    data class Details(val id: String) : Destination("details/$id")
    data class Snackbar(val message: String) : Destination("snackbar")
}

sealed class Route(val label: String) {
    data object App : Route("route_app")
    data object Welcome : Route("route_welcome")
}