package com.example.deezerplayer.di

import androidx.lifecycle.ViewModel
import com.example.deezerplayer.screen.track_list.local_tracks.LocalTracksViewModel
import com.example.deezerplayer.screen.track_list.remote_tracks.RemoteTracksViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @IntoMap
    @ViewModelKey(RemoteTracksViewModel::class)
    @Binds
    fun bindRemoteTracksViewModel(viewModel: RemoteTracksViewModel): ViewModel

    @IntoMap
    @ViewModelKey(LocalTracksViewModel::class)
    @Binds
    fun bindLocalTracksViewModel(viewModel: LocalTracksViewModel): ViewModel

}