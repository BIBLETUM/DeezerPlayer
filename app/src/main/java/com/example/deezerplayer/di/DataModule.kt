package com.example.deezerplayer.di

import com.example.data.local.LocalChartRepositoryImpl
import com.example.data.network.ApiFactory
import com.example.data.network.ApiService
import com.example.data.network.RemoteChartRepositoryImpl
import com.example.domain.repository.ChartRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    @Named("RemoteRepository")
    fun bindRemoteChartRepository(impl: RemoteChartRepositoryImpl): ChartRepository

    @ApplicationScope
    @Binds
    @Named("LocalRepository")
    fun bindLocalChartRepository(impl: LocalChartRepositoryImpl): ChartRepository

    companion object {
        @ApplicationScope
        @Provides
        fun provideApiService(): ApiService {
            return ApiFactory.apiService
        }
    }

}