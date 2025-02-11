package com.example.deezerplayer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
internal fun AppNavGraph(
    navHostController: NavHostController,
    remoteTracksScreen: @Composable () -> Unit,
    localTracksScreen: @Composable () -> Unit,
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
    }
}