package com.example.deezerplayer

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.deezerplayer.component.BottomNavigationBar
import com.example.deezerplayer.component.BottomNavigationBarItem
import com.example.deezerplayer.navigation.AppNavGraph
import com.example.deezerplayer.navigation.NavigationItem
import com.example.deezerplayer.navigation.Screen
import com.example.deezerplayer.screen.player.PlayerScreenRoot
import com.example.deezerplayer.screen.track_list.local_tracks.LocalTracksScreenRoot
import com.example.deezerplayer.screen.track_list.remote_tracks.RemoteTracksScreenRoot
import com.example.deezerplayer.theme.PlayerTheme

@Composable
fun MainScreen() {
    val navHostController = rememberNavController()
    var isBottomNavigationEnabled by rememberSaveable { mutableStateOf(true) }
    val navigationItems = listOf(
        NavigationItem.RemoteMusic, NavigationItem.LocalMusic
    )
    Scaffold(containerColor = PlayerTheme.colors.neutralWhite,
        bottomBar = {
            if (isBottomNavigationEnabled) {
                val backStackEntry by navHostController.currentBackStackEntryAsState()
                BottomNavigationBar {
                    navigationItems.forEach { navigationItem ->
                        val selected = backStackEntry?.destination?.hierarchy?.any {
                            it.route == navigationItem.screen.route
                        } ?: false

                        BottomNavigationBarItem(
                            selected = selected, navigationItem = navigationItem
                        ) {
                            if (!selected) {
                                navHostController.navigate(navigationItem.screen.route) {
                                    popUpTo(Screen.RemoteMusic.route) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    }
                }
            }
        }) { paddingValues ->
        AppNavGraph(
            navHostController = navHostController,
            remoteTracksScreen = {
                RemoteTracksScreenRoot(
                    navigateToPlayer = { trackId, sourceType ->
                        navHostController.navigate(
                            Screen.Player.getRouteWithArgs(
                                trackId,
                                sourceType
                            )
                        ) {
                            popUpTo(Screen.Player.route) { inclusive = true }
                            launchSingleTop = true
                            restoreState = true
                        }

                    },
                    onComposing = {
                        isBottomNavigationEnabled = true
                    },
                    paddingValues = paddingValues,
                )
            },
            localTracksScreen = {
                LocalTracksScreenRoot(
                    navigateToPlayer = { trackId, sourceType ->
                        navHostController.navigate(
                            Screen.Player.getRouteWithArgs(
                                trackId,
                                sourceType
                            )
                        ) {
                            popUpTo(Screen.Player.route) { inclusive = true }
                            launchSingleTop = true
                            restoreState = true
                        }

                    },
                    onComposing = {
                        isBottomNavigationEnabled = true
                    },
                    paddingValues = paddingValues,
                )
            },
            playerScreen = { trackId, trackSourceType ->
                PlayerScreenRoot(
                    trackId = trackId,
                    trackSourceType = trackSourceType,
                    onComposing = {
                        isBottomNavigationEnabled = false
                    },
                    navigateBack = {
                        navHostController.popBackStack()
                    },
                    paddingValues = paddingValues,
                )
            }
        )
    }
}