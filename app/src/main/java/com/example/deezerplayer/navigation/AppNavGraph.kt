package com.example.deezerplayer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.deezerplayer.model.TrackSourceType
import com.example.deezerplayer.navigation.Screen.Companion.KEY_TRACK_ID
import com.example.deezerplayer.navigation.Screen.Companion.KEY_TRACK_SOURCE_TYPE

@Composable
internal fun AppNavGraph(
    navHostController: NavHostController,
    remoteTracksScreen: @Composable () -> Unit,
    localTracksScreen: @Composable () -> Unit,
    playerScreen: @Composable (Long, TrackSourceType) -> Unit,
) {
    NavHost(
        navController = navHostController, startDestination = Screen.RemoteMusic.route
    ) {
        composable(Screen.RemoteMusic.route) {
            remoteTracksScreen()
        }
        composable(Screen.LocalMusic.route) {
            localTracksScreen()
        }
        composable(
            route = Screen.ROUTE_PLAYER,
            arguments = listOf(
                navArgument(KEY_TRACK_ID) { type = NavType.LongType },
                navArgument(KEY_TRACK_SOURCE_TYPE) { type = NavType.StringType }
            )
        ) {
            val trackId = it.arguments?.getLong(KEY_TRACK_ID)
                ?: throw Exception("Incorrect argument for trackId")

            val trackSourceType = it.arguments?.getString(KEY_TRACK_SOURCE_TYPE)
                ?.let { value -> enumValueOf<TrackSourceType>(value) }
                ?: throw Exception("Incorrect argument for trackSourceType")

            playerScreen(trackId, trackSourceType)
        }
    }
}