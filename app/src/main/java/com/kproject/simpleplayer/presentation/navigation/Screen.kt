package com.kproject.simpleplayer.presentation.navigation

const val MediaTypeNameKey = "mediaType"

sealed class Screen(val route: String) {
    data object HomeScreen : Screen("home_screen")
    data object PlayerScreen : Screen(
        "player_screen/{$MediaTypeNameKey}"
    ) {
        fun routeWithArgs(mediaTypeName: String) = "player_screen/$mediaTypeName"
    }
}