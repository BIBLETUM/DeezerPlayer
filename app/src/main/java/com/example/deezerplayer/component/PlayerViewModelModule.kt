package com.example.deezerplayer.component

import androidx.lifecycle.ViewModel
import com.example.deezerplayer.di.ViewModelKey
import com.example.deezerplayer.screen.player.PlayerScreenViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface PlayerViewModelModule {

    @IntoMap
    @ViewModelKey(PlayerScreenViewModel::class)
    @Binds
    fun bindCommentsViewModel(viewModel: PlayerScreenViewModel): ViewModel

}