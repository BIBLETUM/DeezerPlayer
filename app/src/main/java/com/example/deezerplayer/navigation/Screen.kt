package com.example.deezerplayer.navigation

sealed class Screen(
    val route: String,
) {
    data object RemoteMusic : Screen(ROUTE_REMOTE_MUSIC)
    data object LocalMusic : Screen(ROUTE_LOCAL_MUSIC)
    object Player : Screen(ROUTE_PLAYER) {

        private const val ROUTE_FOR_ARGS = "Player_route"

        fun getRouteWithArgs(trackId: Long, sourceType: String): String {
            return ("$ROUTE_FOR_ARGS/$trackId/$sourceType")
        }
    }


    companion object {
        const val ROUTE_REMOTE_MUSIC = "Remote_music_screen_route"
        const val ROUTE_LOCAL_MUSIC = "Local_music_screen_route"

        const val KEY_TRACK_ID = "track_id"
        const val KEY_TRACK_SOURCE_TYPE = "track_type"
        const val ROUTE_PLAYER = "Player_route/{$KEY_TRACK_ID}/{$KEY_TRACK_SOURCE_TYPE}"
    }
}

