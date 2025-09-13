package com.glazer.flying.spaghetti.monster.gospel.bible.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.glazer.flying.spaghetti.monster.gospel.bible.utils.Constants

@Composable
fun NavHostContainer(
    navController: NavHostController,
    listRoutes: List<String>,
    composableContent: NavGraphBuilder.() -> Unit,
    padding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = BottomScreens.Book.route,
        modifier = Modifier,
        enterTransition = {
            val fromIndex = listRoutes.indexOf(initialState.destination.route)
            val toIndex = listRoutes.indexOf(targetState.destination.route)
            val direction = toIndex.compareTo(fromIndex)

            slideInHorizontally(
                animationSpec = tween(durationMillis = Constants.DELAY)
            ) { fullWidth -> if (direction > 0) fullWidth else -fullWidth }
        },
        exitTransition = {
            val fromIndex = listRoutes.indexOf(initialState.destination.route)
            val toIndex = listRoutes.indexOf(targetState.destination.route)
            val direction = toIndex.compareTo(fromIndex)

            slideOutHorizontally(
                animationSpec = tween(durationMillis = Constants.DELAY)
            ) { fullWidth -> if (direction > 0) -fullWidth else fullWidth }
        },
        popEnterTransition = {
            val fromIndex = listRoutes.indexOf(initialState.destination.route)
            val toIndex = listRoutes.indexOf(targetState.destination.route)
            val direction = toIndex.compareTo(fromIndex)

            slideInHorizontally(
                animationSpec = tween(durationMillis = Constants.DELAY)
            ) { fullWidth -> if (direction > 0) fullWidth else -fullWidth }
        },
        popExitTransition = {
            val fromIndex = listRoutes.indexOf(initialState.destination.route)
            val toIndex = listRoutes.indexOf(targetState.destination.route)
            val direction = toIndex.compareTo(fromIndex)

            slideOutHorizontally(
                animationSpec = tween(durationMillis = Constants.DELAY)
            ) { fullWidth -> if (direction > 0) -fullWidth else fullWidth }
        },
        builder = composableContent
    )
}