package com.example.deezerplayer.domain

import com.example.deezerplayer.domain.repository.ChartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRemoteTracksUseCase @Inject constructor(
    private val repository: ChartRepository
) : IGetRemoteTracksUseCase {

    override operator fun invoke(): Flow<List<Track>> {
        return repository.getTracksFlow()
    }

}

fun interface IGetRemoteTracksUseCase {
    operator fun invoke(): Flow<List<Track>>
}