package com.example.domain.use_case

import com.example.domain.Track
import com.example.domain.repository.ChartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class GetLocalTracksFlowUseCase @Inject constructor(
    @Named("LocalRepository") private val repository: ChartRepository
) : IGetLocalTracksFlowUseCase {

    override operator fun invoke(): Flow<List<Track>> {
        return repository.getTracksFlow()
    }

}

interface IGetLocalTracksFlowUseCase {
    operator fun invoke(): Flow<List<Track>>
}