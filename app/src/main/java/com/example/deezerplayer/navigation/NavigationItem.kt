package com.example.deezerplayer.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.deezerplayer.R

enum class NavigationItem(
    val screen: Screen,
    @DrawableRes val iconResId: Int,
    @StringRes val titleOnSelectedResId: Int,
) {
    RemoteMusic(
        screen = Screen.RemoteMusic,
        iconResId = R.drawable.ic_deezer,
        titleOnSelectedResId = R.string.deezer,
    ),

    LocalMusic(
        screen = Screen.LocalMusic,
        iconResId = R.drawable.ic_download,
        titleOnSelectedResId = R.string.downloaded,
    );
}