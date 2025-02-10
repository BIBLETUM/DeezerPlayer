package com.example.deezerplayer.di

import com.example.deezerplayer.domain.GetRemoteTracksUseCase
import com.example.deezerplayer.domain.IGetRemoteTracksUseCase
import dagger.Binds
import dagger.Module

@Module
fun interface DomainModule {

    @ApplicationScope
    @Binds
    fun bindGetChartUseCase(impl: GetRemoteTracksUseCase): IGetRemoteTracksUseCase

}