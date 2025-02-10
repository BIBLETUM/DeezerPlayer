package com.example.deezerplayer.di

import com.example.deezerplayer.domain.GetRemoteTracksUseCase
import com.example.deezerplayer.domain.IGetRemoteTracksUseCase
import com.example.deezerplayer.domain.ISearchRemoteTracksUseCase
import com.example.deezerplayer.domain.SearchRemoteTracksUseCase
import dagger.Binds
import dagger.Module

@Module
interface DomainModule {

    @ApplicationScope
    @Binds
    fun bindGetChartUseCase(impl: GetRemoteTracksUseCase): IGetRemoteTracksUseCase

    @ApplicationScope
    @Binds
    fun bindSearchRemoteTracksUseCase(impl: SearchRemoteTracksUseCase): ISearchRemoteTracksUseCase

}