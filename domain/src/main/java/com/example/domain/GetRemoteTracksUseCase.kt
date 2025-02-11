package com.example.domain

import com.example.domain.repository.ChartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRemoteTracksUseCase @Inject constructor(
    private val repository: ChartRepository
) : IGetRemoteTracksUseCase {

    override operator fun invoke(): Flow<List<Track>> {
        return repository.getTracksFlow()
    }

}

interface IGetRemoteTracksUseCase {
    operator fun invoke(): Flow<List<Track>>
}