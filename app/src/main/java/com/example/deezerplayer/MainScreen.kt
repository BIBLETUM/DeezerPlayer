package com.example.deezerplayer

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.deezerplayer.component.BottomNavigationBar
import com.example.deezerplayer.component.BottomNavigationBarItem
import com.example.deezerplayer.navigation.AppNavGraph
import com.example.deezerplayer.navigation.NavigationItem
import com.example.deezerplayer.navigation.Screen
import com.example.deezerplayer.screen.track_list.local_tracks.LocalTracksScreenRoot
import com.example.deezerplayer.screen.track_list.remote_tracks.RemoteTracksScreenRoot
import com.example.deezerplayer.theme.PlayerTheme

@Composable
fun MainScreen() {
    val navHostController = rememberNavController()
    val navigationItems = listOf(
        NavigationItem.RemoteMusic, NavigationItem.LocalMusic
    )
    Scaffold(containerColor = PlayerTheme.colors.neutralWhite,
        bottomBar = {
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
        }) { paddingValues ->
        AppNavGraph(
            navHostController = navHostController,
            remoteTracksScreen = {
                RemoteTracksScreenRoot(
                    paddingValues = paddingValues,
                )
            },
            localTracksScreen = {
                LocalTracksScreenRoot(
                    paddingValues = paddingValues,
                )
            },
        )
    }
}