package com.example.deezerplayer.di

import com.example.deezerplayer.ViewModelFactory
import com.example.deezerplayer.component.PlayerViewModelModule
import com.example.deezerplayer.model.TrackSourceType
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [PlayerViewModelModule::class])
interface PlayerScreenComponent {

    fun getViewModelFactory(): ViewModelFactory

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance trackId: Long,
            @BindsInstance trackSourceType: TrackSourceType,
        ): PlayerScreenComponent
    }
}