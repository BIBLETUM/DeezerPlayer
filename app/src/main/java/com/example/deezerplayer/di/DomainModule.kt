package com.example.deezerplayer.di

import com.example.domain.GetRemoteTracksFlowUseCase
import com.example.domain.IGetRemoteTracksFlowUseCase
import com.example.domain.ISearchRemoteTracksUseCase
import com.example.domain.SearchRemoteTracksUseCase
import dagger.Binds
import dagger.Module

@Module
interface DomainModule {

    @ApplicationScope
    @Binds
    fun bindGetChartUseCase(impl: GetRemoteTracksFlowUseCase): IGetRemoteTracksFlowUseCase

    @ApplicationScope
    @Binds
    fun bindSearchRemoteTracksUseCase(impl: SearchRemoteTracksUseCase): ISearchRemoteTracksUseCase

}