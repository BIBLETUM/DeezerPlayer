package com.example.deezerplayer.screen.player

import android.app.ActivityManager
import android.content.Context

@SuppressWarnings("deprecation")
fun isServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
    val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    for (service in manager.getRunningServices(Int.MAX_VALUE)) {
        if (service.service.className == serviceClass.name) {
            return true
        }
    }
    return false
}
