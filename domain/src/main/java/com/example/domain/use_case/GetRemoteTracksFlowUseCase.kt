package com.example.domain.use_case

import com.example.domain.Track
import com.example.domain.repository.ChartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class GetRemoteTracksFlowUseCase @Inject constructor(
    @Named("RemoteRepository") private val repository: ChartRepository
) : IGetRemoteTracksFlowUseCase {

    override operator fun invoke(): Flow<List<Track>> {
        return repository.getTracksFlow()
    }

}

interface IGetRemoteTracksFlowUseCase {
    operator fun invoke(): Flow<List<Track>>
}