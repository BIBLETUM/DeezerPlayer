package com.example.deezerplayer.di

import com.example.domain.use_case.GetLastLocalTracksListUseCase
import com.example.domain.use_case.GetLastRemoteTracksListUseCase
import com.example.domain.use_case.GetLocalTracksFlowUseCase
import com.example.domain.use_case.GetRemoteTracksFlowUseCase
import com.example.domain.use_case.IGetLastLocalTracksListUseCase
import com.example.domain.use_case.IGetLastRemoteTracksListUseCase
import com.example.domain.use_case.IGetLocalTracksFlowUseCase
import com.example.domain.use_case.IGetRemoteTracksFlowUseCase
import com.example.domain.use_case.ISearchLocalTracksUseCase
import com.example.domain.use_case.ISearchRemoteTracksUseCase
import com.example.domain.use_case.SearchLocalTracksUseCase
import com.example.domain.use_case.SearchRemoteTracksUseCase
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

    @ApplicationScope
    @Binds
    fun bindGetLocalTracksFlowUseCase(impl: GetLocalTracksFlowUseCase): IGetLocalTracksFlowUseCase

    @ApplicationScope
    @Binds
    fun bindSearchLocalTracksUseCase(impl: SearchLocalTracksUseCase): ISearchLocalTracksUseCase

    @ApplicationScope
    @Binds
    fun bindGetLastRemoteTracksListUseCase(impl: GetLastRemoteTracksListUseCase): IGetLastRemoteTracksListUseCase

    @ApplicationScope
    @Binds
    fun bindGetLastLocalTracksListUseCase(impl: GetLastLocalTracksListUseCase): IGetLastLocalTracksListUseCase

}