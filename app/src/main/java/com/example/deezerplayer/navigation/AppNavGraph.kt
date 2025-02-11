package com.example.deezerplayer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.deezerplayer.navigation.Screen.Companion.KEY_TRACK_ID
import com.example.deezerplayer.navigation.Screen.Companion.KEY_TRACK_SOURCE_TYPE

@Composable
internal fun AppNavGraph(
    navHostController: NavHostController,
    remoteTracksScreen: @Composable () -> Unit,
    localTracksScreen: @Composable () -> Unit,
    playerScreen: @Composable (Long, String) -> Unit,
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
        composable(route = Screen.Player.route) {
            val trackId = it.arguments?.getString(KEY_TRACK_ID)?.toLongOrNull() ?: throw Exception(
                "Incorrect argument for trackId"
            )
            val trackSourceType = it.arguments?.getString(KEY_TRACK_SOURCE_TYPE) ?: throw Exception(
                "Incorrect argument for trackSourceType"
            )
            playerScreen(
                trackId,
                trackSourceType
            )
        }
    }
}