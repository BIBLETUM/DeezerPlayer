package com.example.deezerplayer.di

import com.example.deezerplayer.data.ChartRepositoryImpl
import com.example.deezerplayer.data.network.ApiFactory
import com.example.deezerplayer.data.network.ApiService
import com.example.deezerplayer.domain.repository.ChartRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
fun interface DataModule {

    @ApplicationScope
    @Binds
    fun bindChartRepository(impl: ChartRepositoryImpl): ChartRepository

    companion object {
        @ApplicationScope
        @Provides
        fun provideApiService(): ApiService {
            return ApiFactory.apiService
        }
    }

}