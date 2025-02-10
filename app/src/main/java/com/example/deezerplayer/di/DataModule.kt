package com.example.deezerplayer.di

import com.example.data.ChartRepositoryImpl
import com.example.data.network.ApiFactory
import com.example.data.network.ApiService
import com.example.domain.repository.ChartRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

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