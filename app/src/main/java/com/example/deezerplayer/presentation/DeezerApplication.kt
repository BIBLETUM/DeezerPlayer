package com.example.deezerplayer.presentation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.deezerplayer.di.ApplicationComponent
import com.example.deezerplayer.di.DaggerApplicationComponent

class DeezerApplication : Application() {

    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(
            this
        )
    }
}

@Composable
fun getApplicationComponent(): ApplicationComponent {
    return (LocalContext.current.applicationContext as DeezerApplication).component
}