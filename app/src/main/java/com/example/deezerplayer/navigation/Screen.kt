package com.example.deezerplayer.navigation

sealed class Screen(
    val route: String,
) {
    data object RemoteMusic : Screen(ROUTE_REMOTE_MUSIC)
    data object LocalMusic : Screen(ROUTE_LOCAL_MUSIC)


    companion object {
        const val ROUTE_REMOTE_MUSIC = "Remote_music_screen_route"
        const val ROUTE_LOCAL_MUSIC = "Local_music_screen_route"
    }
}