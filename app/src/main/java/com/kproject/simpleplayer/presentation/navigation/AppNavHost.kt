package com.kproject.simpleplayer.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kproject.simpleplayer.presentation.screens.home.HomeScreen
import com.kproject.simpleplayer.presentation.screens.player.PlayerScreen
import com.kproject.simpleplayer.presentation.screens.player.model.MediaType

private const val ANIMATION_DURATION = 700

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route
    ) {
        // HomeScreen
        composable(
            route = Screen.HomeScreen.route
        ) { navBackStackEntry ->
            HomeScreen(
                onNavigateToPlayerScreen = { mediaType ->
                    if (navBackStackEntry.lifecycleIsResumed()) {
                        navController.navigate(Screen.PlayerScreen.routeWithArgs(mediaType.name))
                    }
                }
            )
        }

        composable(
            route = Screen.PlayerScreen.route,
            arguments = listOf(
                navArgument(name = MediaTypeNameKey) {
                    type = NavType.StringType
                },
            ),
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(durationMillis = ANIMATION_DURATION)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(durationMillis = ANIMATION_DURATION)
                )
            }
        ) { navBackStackEntry ->
            navBackStackEntry.arguments?.let { bundle ->
                PlayerScreen(
                    mediaType = MediaType.valueOf(bundle.getString(MediaTypeNameKey) ?: ""),
                    onNavigateBack = {
                        if (navBackStackEntry.lifecycleIsResumed()) {
                            navController.popBackStack()
                        }
                    }
                )
            }
        }
    }
}

private fun NavBackStackEntry.lifecycleIsResumed() =
        this.lifecycle.currentState == Lifecycle.State.RESUMED