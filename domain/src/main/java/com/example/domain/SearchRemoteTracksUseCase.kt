package com.example.domain

import com.example.domain.repository.ChartRepository
import javax.inject.Inject

class SearchRemoteTracksUseCase @Inject constructor(
    private val repository: ChartRepository
) : ISearchRemoteTracksUseCase {

    override suspend operator fun invoke(query: String) {
        repository.searchTracks(query)
    }

}

interface ISearchRemoteTracksUseCase {
    suspend operator fun invoke(query: String)
}