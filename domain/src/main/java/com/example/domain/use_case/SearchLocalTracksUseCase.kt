package com.example.domain.use_case

import com.example.domain.repository.ChartRepository
import javax.inject.Inject
import javax.inject.Named

class SearchLocalTracksUseCase @Inject constructor(
    @Named("LocalRepository") private val repository: ChartRepository
) : ISearchLocalTracksUseCase {

    override suspend operator fun invoke(query: String) {
        repository.searchTracks(query)
    }

}

interface ISearchLocalTracksUseCase {
    suspend operator fun invoke(query: String)
}