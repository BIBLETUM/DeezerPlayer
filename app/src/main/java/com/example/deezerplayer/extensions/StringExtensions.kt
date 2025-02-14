package com.example.deezerplayer.extensions

import android.net.Uri

fun String.encode(): String {
    return Uri.encode(this)
}