package com.example.deezerplayer.di

import com.example.domain.GetRemoteTracksUseCase
import com.example.domain.IGetRemoteTracksUseCase
import com.example.domain.ISearchRemoteTracksUseCase
import com.example.domain.SearchRemoteTracksUseCase
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