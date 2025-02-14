package com.example.domain.use_case

import com.example.domain.repository.ChartRepository
import javax.inject.Inject
import javax.inject.Named

class SearchRemoteTracksUseCase @Inject constructor(
    @Named("RemoteRepository") private val repository: ChartRepository
) : ISearchRemoteTracksUseCase {

    override suspend operator fun invoke(query: String) {
        repository.searchTracks(query)
    }

}

interface ISearchRemoteTracksUseCase {
    suspend operator fun invoke(query: String)
}