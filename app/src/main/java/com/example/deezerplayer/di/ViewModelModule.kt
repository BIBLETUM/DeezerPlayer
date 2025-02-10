package com.example.deezerplayer.di

import androidx.lifecycle.ViewModel
import com.example.deezerplayer.presentation.screen.remote_tracks.RemoteTracksViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @IntoMap
    @ViewModelKey(RemoteTracksViewModel::class)
    @Binds
    fun bindRemoteTracksViewModel(viewModel: RemoteTracksViewModel): ViewModel

}